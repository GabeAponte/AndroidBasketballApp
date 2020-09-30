package basketballCounter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "SaveActivity"
private const val EXTRA_Team_A_Name = "team_a_name"
private const val EXTRA_Team_B_Name = "team_b_name"
private const val EXTRA_Team_A_Score = "team_a_score"
private const val EXTRA_Team_B_Score = "team_b_score"
private const val EXTRA_Game_UUID = "game_uuid"
private const val EXTRA_IS_NEW_GAME = "isNewGame"
const val EXTRA_Score_Saved_UUID = "score_saved"

/**
 * SaveActivity class that holds the functions for displaying scores and communicating with
 * BasketballGameFragment to receive and send data. It also connects to the db and saves the game data that is displayed
 */
class SaveActivity : AppCompatActivity() {

    // UUID of the game data to be displayed and boolean of if its a new game or not
    private var gameID: UUID? = null
    private var isNewGame : Boolean? = null

    // declares all the UI elements that will be manipulated
    private lateinit var mainLabel: TextView
    private lateinit var teamAName: TextView
    private lateinit var teamBName: TextView
    private lateinit var teamAScoreLabel: TextView
    private lateinit var teamBScoreLabel: TextView
    private lateinit var backBtn: Button

    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }

    /**
     * Overrides the onCreate method to load the data sent from MainActivity and to add manipulations to UI elements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_save)

        // Initialize the UI elements via their reference id's
        mainLabel = findViewById(R.id.main_label)
        teamAName = findViewById(R.id.team_a_name_save)
        teamBName = findViewById(R.id.team_b_name_save)
        teamAScoreLabel = findViewById(R.id.score_a_label_save)
        teamBScoreLabel = findViewById(R.id.score_b_label_save)
        backBtn = findViewById(R.id.back_btn)

        // set the team names, scores, ID and isNewGame based off the data stored in intent Extra's from MainActivity
        teamAName.text = intent.getStringExtra(EXTRA_Team_A_Name)
        teamBName.text = intent.getStringExtra(EXTRA_Team_B_Name)
        teamAScoreLabel.text = intent.getStringExtra(EXTRA_Team_A_Score)
        teamBScoreLabel.text = intent.getStringExtra(EXTRA_Team_B_Score)
        gameID = intent.getSerializableExtra(EXTRA_Game_UUID) as UUID?
        Log.d(TAG, gameID.toString())
        isNewGame = intent.getBooleanExtra(EXTRA_IS_NEW_GAME, false)

        // set the mainLabel to updated or saved based on if a UUID was passed in or not
        if(isNewGame == false) mainLabel.setText(R.string.updated_label)
        else if(isNewGame == true) mainLabel.setText(R.string.saved_label)

        //On Click Listeners for all the buttons
        backBtn.setOnClickListener { super.onBackPressed() }

        // scores were shown, so save them and send a result to MainActivity
        saveScores()
    }

    /**
     * Function to save the scores shown on screen and send the UUID back to the BasketballGameFragment
     */
    private fun saveScores() {
        val basketballGameRepository = BasketballGameRepository.get()

        // create the game to be saved or updated
        val game = BasketballGame(gameID!!, Integer.valueOf(teamAScoreLabel.text.toString()),
            Integer.valueOf(teamBScoreLabel.text.toString()), teamAName.text.toString(), teamBName.text.toString(), Calendar.getInstance().time)
        gameDetailViewModel.saveGame(game)

        // if isNewGame is false, update the existing game
        if(!isNewGame!!){
            gameDetailViewModel.saveGame(game)
            setScoreSavedShownResult(game.id)
        }
        // if isNewGame is true, add the new game
        else if(isNewGame!!){
            basketballGameRepository.addGame(game)
            setScoreSavedShownResult(game.id)
        }
    }

    /**
     * Companion object with the newIntent function that MainActivity calls in order to pass data to this activity
     */
    companion object {
        // intent with all the game data passed by  BasketballGameFragment
        fun newIntent(packageContext: Context, teamAName: String, teamBName: String, teamAScore: Int, teamBScore: Int, gameID: UUID, isNewGame: Boolean): Intent {
            Log.d(TAG, "newIntent() called")
            return Intent(packageContext, SaveActivity::class.java).apply {
                putExtra(EXTRA_Team_A_Name, teamAName)
                putExtra(EXTRA_Team_B_Name, teamBName)
                putExtra(EXTRA_Team_A_Score, teamAScore.toString())
                putExtra(EXTRA_Team_B_Score, teamBScore.toString())
                putExtra(EXTRA_Game_UUID, gameID)
                putExtra(EXTRA_IS_NEW_GAME, isNewGame)
            }
        }
    }

    /**
     * Method that states the scores have been saved/shown on screen and MainActivity needs a result
     */
    private fun setScoreSavedShownResult(gameID: UUID) {
        val data = Intent().apply {
            putExtra(EXTRA_Score_Saved_UUID, gameID)
        }
        setResult(Activity.RESULT_OK, data)
        Log.d(TAG, "setResult() called")
    }

    /**
     * Adding logs messages for startActivityForResult() onStart, onResume, onPause, onStop and onDestroy
     */
    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        Log.d(TAG, "startActivityForResult(intent, requestCode) called")
    }

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
