package basketballCounter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "BasketballGameFragment"
private const val TEAM_A_SCORE = "team_a_score"
private const val TEAM_B_SCORE = "team_b_score"
private const val REQUEST_CODE_SAVE = 0
private const val ARG_GAME_ID = "gameID"

/**
 * Fragment class for the BasketballCounter that controls the main functionality
 */
class BasketballGameFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onDisplayClicked(winningTeam : String)
        val getGameListViewModel : GameListViewModel
    }

    private var callbacks: Callbacks? = null
    private var currentWinner = ""
    private var gameIdArg: UUID? = null // the current UUID for a game being edited
    private var argCount: Int = 0 // used to make sure that the arg vars only populate the UI once

    // declares all the UI elements that will be manipulated
    private lateinit var backgroundScrollView: ScrollView
    private lateinit var teamAName: EditText
    private lateinit var teamBName: EditText
    private lateinit var teamAScoreLabel: TextView
    private lateinit var teamBScoreLabel: TextView
    private lateinit var threePointsABtn: Button
    private lateinit var threePointsBBtn: Button
    private lateinit var twoPointsABtn: Button
    private lateinit var twoPointsBBtn: Button
    private lateinit var freeThrowABtn: Button
    private lateinit var freeThrowBBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var displayBtn: Button

    // game object that is set from an observer when this view is created after a game is selected in the GameListFragment
    private lateinit var game: BasketballGame

    /**
     * ViewModel initializations
     */
    private val basketballGameViewModel: BasketballGameViewModel by lazy {
        ViewModelProviders.of(this).get(BasketballGameViewModel::class.java)
    }
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }

    /**
     * Overrides the onCreate method to log when it was called and set the value
     * for the gameId argument that was passed from GameListFragment and load the game
     * from that ID
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        if (arguments != null) {
            gameIdArg = arguments?.getSerializable(ARG_GAME_ID) as? UUID
            Log.d(TAG, "args bundle gameIdArg: $gameIdArg")
            gameDetailViewModel.loadGame(gameIdArg!!)
        }
    }
    /**
     * Overrides the onAttach method to log when it was called and set callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        Log.d(TAG, "onAttach() called")
    }

    /**
     * Overrides the onDetach method to log when it was called and set callbacks
     */
    override fun onDetach() {
        super.onDetach()
        callbacks = null
        Log.d(TAG, "onDetach() called")
    }

    /**
     * Overrides the onCreateView method to reload saved data / set initial scores and to add manipulations to UI elements
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basketball_game, container, false)
        Log.d(TAG, "onCreateView() called")

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val basketballGameViewModel = provider.get(BasketballGameViewModel::class.java)
        Log.d(TAG, "Got a BasketballGameViewModel: $basketballGameViewModel")

        // Initialize the UI elements via their reference id's
        backgroundScrollView = view.findViewById(R.id.scrollView)
        teamAName = view.findViewById(R.id.team_a_name)
        teamBName = view.findViewById(R.id.team_b_name)
        teamAScoreLabel = view.findViewById(R.id.score_a_label)
        teamBScoreLabel = view.findViewById(R.id.score_b_label)
        threePointsABtn = view.findViewById(R.id.three_points_a_btn)
        threePointsBBtn = view.findViewById(R.id.three_points_b_btn)
        twoPointsABtn = view.findViewById(R.id.two_points_a_btn)
        twoPointsBBtn = view.findViewById(R.id.two_points_b_btn)
        freeThrowABtn = view.findViewById(R.id.free_throw_a_btn)
        freeThrowBBtn = view.findViewById(R.id.free_throw_b_btn)
        resetBtn = view.findViewById(R.id.reset_btn)
        saveBtn = view.findViewById(R.id.save_btn)
        displayBtn = view.findViewById(R.id.display_btn)

        // Populate the scores with any savedInstanceStated data (if there is any)
        if (savedInstanceState != null){
            // If a previous score exists, save them to the view model. If not, set the score to zero.
            basketballGameViewModel.setScoreA(savedInstanceState.getInt(TEAM_A_SCORE, 0))
            basketballGameViewModel.setScoreB(savedInstanceState.getInt(TEAM_B_SCORE, 0))
        }

        // Set the initial score of the game to the current score stored in the ViewModel
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
        teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString()

        //On Click Listeners for all the buttons
        threePointsABtn.setOnClickListener { addPointsScoreA(3) }
        threePointsBBtn.setOnClickListener { addPointsScoreB(3) }
        twoPointsABtn.setOnClickListener { addPointsScoreA(2) }
        twoPointsBBtn.setOnClickListener { addPointsScoreB(2) }
        freeThrowABtn.setOnClickListener { addPointsScoreA(1) }
        freeThrowBBtn.setOnClickListener { addPointsScoreB(1) }
        resetBtn.setOnClickListener { resetUI() }

        // onClickListener for the save button that creates a new intent to start the SaveActivity and send the proper data
        saveBtn.setOnClickListener { onSaveClicked() }

        // show the GameListFragment when display button is clicked by calling the function in MainActivity
        displayBtn.setOnClickListener {
            Log.d(TAG, "displayBtn.onClick() called")
            callbacks?.onDisplayClicked(currentWinner)
        }

        updateBackground()
        updateWinningTeam()

        return view
    }

    /**
     * Override for the onViewCreated method that observes the selected game from the GameListFragment
     * and updates the UI based on its values
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer { game -> game?.let {
                    this.game = game
                    updateUI()
                }
            })
    }

    /**
     * Function that is called by the observer in onViewCreated and updates the UI elements to
     * display game data from the game selected in GameListFragment
     */
    private fun updateUI() {
        // if there is a gameIdArg, set the save button text to Update
        if (gameIdArg != null) saveBtn.setText(R.string.update_label)

        // if there is a gameIdArg,and it has not been used to update the UI yet, update the UI
        if (gameIdArg != null && argCount == 0) {
            teamAName.setText(game.teamAName)
            teamBName.setText(game.teamBName)
            basketballGameViewModel.setScoreA(game.teamAScore)
            basketballGameViewModel.setScoreB(game.teamBScore)
            teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
            teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString()
            updateBackground()
            updateWinningTeam()
            argCount++
        }
    }

    /**
     * Overrides the onActivityResult method in order to receive a code from SaveActivity.
     * Shows a toast depending on if the score new and saved, or old and updated
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult() called")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_SAVE) {
            // if there is no gameIdArg value, we just saved new game data
            if (gameIdArg == null) {
                Toast.makeText(activity, "New Game Data Has Been Saved!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Saved Toast shown")
            }
            // if there is a gameIdArg value, we just updated old game data
            else if (gameIdArg != null) {
                Toast.makeText(activity, "Previous Game Data Has Been Updated!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Updated Toast shown")
            }
            // now set the gameIdArg to the UUID passed back from the SaveActivity
            gameIdArg = data!!.getSerializableExtra(EXTRA_Score_Saved_UUID) as UUID?
            saveBtn.setText(R.string.update_label)
        }
    }

    /**
     * Overrides the onSaveInstanceState method in order to save the scores whenever the activity is killed
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState(Bundle) called")

        //Save the current scores whenever the app is stopped
        savedInstanceState.putInt(TEAM_A_SCORE, basketballGameViewModel.teamACurrentScore)
        savedInstanceState.putInt(TEAM_B_SCORE, basketballGameViewModel.teamBCurrentScore)
    }

    /**
     * Companion object that creates a new instance of BasketballGameFragment
     */
    companion object {
        fun newInstance(): BasketballGameFragment {
            return BasketballGameFragment()
        }

        // pass in the GameID to display specific game data
        fun newInstance(gameId : UUID): BasketballGameFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_ID, gameId)
            }
            return BasketballGameFragment().apply {
                arguments = args
            }
        }
    }

    /**
     * Function called when teh save button is clicked. It creates a new intent to
     * start the SaveActivity and send the proper data
     */
    private fun onSaveClicked() {
        var teamA = teamAName.text.toString()
        var teamB = teamBName.text.toString()

        //If the name is not specified(ie. the hint text is displayed) send the hint text
        if (teamA.trim() == "") teamA = "Team A"
        if (teamB.trim() == "") teamB = "Team B"

        val teamAScore = basketballGameViewModel.teamACurrentScore
        val teamBScore = basketballGameViewModel.teamBCurrentScore

        // send an intent to the Save Activity. If the gama data is being edited, send the correlating a UUID
        if(gameIdArg != null) {
            val intent = activity?.let { context ->
                SaveActivity.newIntent(context, teamA, teamB, teamAScore, teamBScore, gameIdArg!!)
            }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }
        // if the game data is brand new, don't send a UUID as there isn't one
        else if(gameIdArg == null) {
            val intent = activity?.let { context ->
                SaveActivity.newIntent(context, teamA, teamB, teamAScore, teamBScore)
            }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }
    }

    /**
     * Adds points to team A's score by taking in an int value from the button click
     * Calls the addPointsScoreA method in the basketballGameViewModel and then sets the text
     * to the current score stored in the basketballGameViewModel
     */
    private fun addPointsScoreA(points: Int) {
        basketballGameViewModel.addPointsScoreA(points)
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
        updateBackground()
        updateWinningTeam()
    }

    /**
     * Adds points to team B's score by taking in an int value from the button click
     * Calls the addPointsScoreB method in the basketballGameViewModel and then sets the text
     * to the current score stored in the basketballGameViewModel
     */
    private fun addPointsScoreB(points: Int) {
        basketballGameViewModel.addPointsScoreB(points)
        teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString()
        updateBackground()
        updateWinningTeam()
    }

    /**
     * Resets the UI for the screen by calling the resetScores method in the
     * basketballGameViewModel, resetting the UI elements and making gameIdArg null
    */
    private fun resetUI() {
        basketballGameViewModel.resetScores()
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
        teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString()
        teamAName.setText("")
        teamBName.setText("")
        saveBtn.setText(R.string.save_label)
        updateBackground()
        updateWinningTeam()
        gameIdArg = null
    }

    /**
     * Changes the background color of the app whenever the score changes and one of the conditions are met
     */
    private fun updateBackground() {
        if (basketballGameViewModel.teamACurrentScore == basketballGameViewModel.teamBCurrentScore) backgroundScrollView.setBackgroundResource(R.color.gray)
        if (basketballGameViewModel.teamACurrentScore > basketballGameViewModel.teamBCurrentScore) backgroundScrollView.setBackgroundResource(R.color.celticsGreen)
        if (basketballGameViewModel.teamACurrentScore < basketballGameViewModel.teamBCurrentScore) backgroundScrollView.setBackgroundResource(R.color.lakersPurple)
    }

    /**
     * Changes the current winner of the game whenever the score changes and one of the conditions are met
     */
    private fun updateWinningTeam() {
        if (basketballGameViewModel.teamACurrentScore == basketballGameViewModel.teamBCurrentScore) currentWinner = "Tie"
        if (basketballGameViewModel.teamACurrentScore > basketballGameViewModel.teamBCurrentScore) currentWinner = "A"
        if (basketballGameViewModel.teamACurrentScore < basketballGameViewModel.teamBCurrentScore) currentWinner = "B"
    }

    /**
     * Adding logs messages for onStart, onResume, onPause, onStop and onDestroy
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}
