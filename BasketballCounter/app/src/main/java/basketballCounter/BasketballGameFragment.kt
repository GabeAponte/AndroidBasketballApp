package basketballCounter

import android.app.Activity
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

private const val TAG = "BasketballGameFragment"
private const val TEAM_A_SCORE = "team_a_score"
private const val TEAM_B_SCORE = "team_b_score"
private const val REQUEST_CODE_SAVE = 0

private const val MAIN_FRAG_TAG = "MainFrag"
private const val LIST_FRAG_TAG = "ListFrag"

/**
 * Fragment class for the BasketballCounter that controls the main functionality
 */
class BasketballGameFragment : Fragment() {

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

    private val basketballGameViewModel: BasketballGameViewModel by lazy {
        ViewModelProviders.of(this).get(BasketballGameViewModel::class.java)
    }

    /**
     * Overrides the onCreate method to log when it was called
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

    }

    /**
     * Overrides the onCreateView method to reload saved data / set initial scores and to add manipulations to UI elements
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basketball_game, container, false)

        // If a previous score exists, save them to the view model. If not, set the score to zero.
        basketballGameViewModel.setScoreA(savedInstanceState?.getInt(TEAM_A_SCORE, 0) ?: 0)
        basketballGameViewModel.setScoreB(savedInstanceState?.getInt(TEAM_B_SCORE, 0) ?: 0)

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

        // Set the initial score of the game to the current score stored in the ViewModel
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString();
        teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString();

        //On Click Listeners for all the buttons
        threePointsABtn.setOnClickListener { addPointsScoreA(3) }
        threePointsBBtn.setOnClickListener { addPointsScoreB(3) }
        twoPointsABtn.setOnClickListener { addPointsScoreA(2) }
        twoPointsBBtn.setOnClickListener { addPointsScoreB(2) }
        freeThrowABtn.setOnClickListener { addPointsScoreA(1) }
        freeThrowBBtn.setOnClickListener { addPointsScoreB(1) }
        resetBtn.setOnClickListener { resetScores() }

        // onClickListener for the save button that creates a new intent to start the SaveActivity and send the proper data
        saveBtn.setOnClickListener {
            var teamA = teamAName.text.toString()
            var teamB = teamBName.text.toString()

            //If the name is not specified(ie. the hint text is displayed) send the hint text
            if (teamA.trim() == "") teamA = "Team A"
            if (teamB.trim() == "") teamA = "Team B"

            val teamAScore = basketballGameViewModel.teamACurrentScore
            val teamBScore = basketballGameViewModel.teamBCurrentScore
            val intent = activity?.let { context ->
                SaveActivity.newIntent(context, teamA, teamB, teamAScore, teamBScore)
            }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        // show the GameListFragment when display button is clicked and add it to the backstack
        // and hide the main basketball game fragment
        displayBtn.setOnClickListener {
            fragmentManager?.findFragmentByTag(LIST_FRAG_TAG)?.let { listFragment ->
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.show(listFragment)
                    ?.addToBackStack(LIST_FRAG_TAG)
                    ?.hide(fragmentManager?.findFragmentByTag(MAIN_FRAG_TAG)!!)
                    ?.commit()
            }
        }

        updateBackground();

        return view
    }

    /**
     * Overrides the onActivityResult method in order to receive a code from SaveActivity.
     * If the data from that code is true, show a toast.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult() called")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_SAVE) {
            if (data!!.getBooleanExtra(EXTRA_Score_Saved, false)) {
                Toast.makeText(
                    activity,
                    "Actually Saving Scores Will be Added in Programming 4",
                    Toast.LENGTH_LONG
                )
                    .show()
                Log.d(TAG, "Toast shown")
            }
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
     * Adds points to team A's score by taking in an int value from the button click
     * Calls the addPointsScoreA method in the basketballGameViewModel and then sets the text
     * to the current score stored in the basketballGameViewModel
     */
    private fun addPointsScoreA(points: Int) {
        basketballGameViewModel.addPointsScoreA(points)
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
        updateBackground()
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
    }

    /**
     * Resets the score for both teams by calling the resetScores method in the
     * basketballGameViewModel and then sets the text for each score
     */
    private fun resetScores() {
        basketballGameViewModel.resetScores()
        teamAScoreLabel.text = basketballGameViewModel.teamACurrentScore.toString()
        teamBScoreLabel.text = basketballGameViewModel.teamBCurrentScore.toString()
        updateBackground()
    }

    /**
     * Changes the background color of the app whenever the score changes and one of the conditions are met
     */
    private fun updateBackground() {
        if (basketballGameViewModel.teamACurrentScore === basketballGameViewModel.teamBCurrentScore) {
            backgroundScrollView.setBackgroundResource(R.color.gray)
        }

        if (basketballGameViewModel.teamACurrentScore > basketballGameViewModel.teamBCurrentScore) {
            backgroundScrollView.setBackgroundResource(R.color.celticsGreen)
        }
        if (basketballGameViewModel.teamACurrentScore < basketballGameViewModel.teamBCurrentScore) {
            backgroundScrollView.setBackgroundResource(R.color.lakersPurple)
        }
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
