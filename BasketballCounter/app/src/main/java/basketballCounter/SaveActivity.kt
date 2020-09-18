package basketballCounter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView

private const val TAG = "SaveActivity"
private const val EXTRA_Team_A_Name = "team_a_name"
private const val EXTRA_Team_B_Name = "team_b_name"
private const val EXTRA_Team_A_Score = "team_a_score"
private const val EXTRA_Team_B_Score = "team_b_score"
const val EXTRA_Score_Saved = "score_saved"

/**
 * SaveActivity class that holds the functions for displaying scores that will be saved and communicating with
 * BasketballGameFragment to receive and send data
 */
class SaveActivity : AppCompatActivity() {

    // declares all the UI elements that will be manipulated
    private lateinit var backgroundScrollView: ScrollView
    private lateinit var teamAName: TextView
    private lateinit var teamBName: TextView
    private lateinit var teamAScoreLabel: TextView
    private lateinit var teamBScoreLabel: TextView
    private lateinit var backBtn: Button

    /**
     * Overrides the onCreate method to load the data sent from MainActivity and to add manipulations to UI elements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_save)

        // Initialize the UI elements via their reference id's
        backgroundScrollView = findViewById(R.id.scrollView_save)
        teamAName = findViewById(R.id.team_a_name_save)
        teamBName = findViewById(R.id.team_b_name_save)
        teamAScoreLabel = findViewById(R.id.score_a_label_save)
        teamBScoreLabel = findViewById(R.id.score_b_label_save)
        backBtn = findViewById(R.id.back_btn)

        // set the team names and scores based off the data stored in intent Extra's from MainActivity
        teamAName.text = intent.getStringExtra(EXTRA_Team_A_Name)
        teamBName.text = intent.getStringExtra(EXTRA_Team_B_Name)
        teamAScoreLabel.text = intent.getStringExtra(EXTRA_Team_A_Score)
        teamBScoreLabel.text = intent.getStringExtra(EXTRA_Team_B_Score)

        //On Click Listeners for all the buttons
        backBtn.setOnClickListener { super.onBackPressed() }

        // scores were shown, so send a result to MainActivity
        setScoreSavedShownResult(true)
    }

    /**
     * Companion object with the newIntent function that MainActivity calls in order to pass data to this activity
     */
    companion object {
        fun newIntent(packageContext: Context, teamAName: String, teamBName: String, teamAScore: Int, teamBScore: Int): Intent {
            Log.d(TAG, "newIntent() called")
            return Intent(packageContext, SaveActivity::class.java).apply {
                putExtra(EXTRA_Team_A_Name, teamAName)
                putExtra(EXTRA_Team_B_Name, teamBName)
                putExtra(EXTRA_Team_A_Score, teamAScore.toString())
                putExtra(EXTRA_Team_B_Score, teamBScore.toString())
            }
        }
    }

    /**
     * Method that states the scores have been saved/shown on screen and MainActivity needs a result
     */
    private fun setScoreSavedShownResult(isScoreSaved: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_Score_Saved, isScoreSaved)
        }
        setResult(Activity.RESULT_OK, data)
        Log.d(TAG, "setResult() called")
    }

    /**
     * Adding logs messages for startActivityForResult() onStart, onResume, onPause, onStop and onDestroy
     */

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        Log.d(TAG, "onCreate(intent, requestCode) called")
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
