package basketballCounter

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG =  "BasketballGameViewModel"

/**
 * BasketballGameViewModel
 */
class BasketballGameViewModel : ViewModel() {

    // the basketballGame object to be used by the view model
    private val basketballGame = BasketballGame(UUID.randomUUID(),0, 0, "TeamA", "TeamB", Calendar.getInstance().time)

    // logs when the view model is created
    init {
        Log.d(TAG, "Instance created")
    }

    /**
     * increases the score for team A by adding a passed in int value to the score held in the basketball game object
     */
    fun addPointsScoreA(points: Int) {
        basketballGame.teamAScore += points
    }

    /**
     * increases the score for team B by adding a passed in int value to the score held in the basketball game object
     */
    fun addPointsScoreB(points: Int) {
        basketballGame.teamBScore += points
    }

    /**
     * Reset the score of the game by setting each score to 0
     */
    fun resetScores() {
        basketballGame.teamAScore = 0
        basketballGame.teamBScore = 0
    }

    /**
     * Getter for team A score
     */
    val teamACurrentScore: Int
        get() = basketballGame.teamAScore

    /**
     * Setter for team A score
     */
    fun setScoreA(points:Int) {
        basketballGame.teamAScore = points
    }

    /**
     * Getter for team B score
     */
    val teamBCurrentScore: Int
        get() = basketballGame.teamBScore

    /**
     * Setter for team B score
     */
    fun setScoreB(points:Int) {
        basketballGame.teamBScore = points
    }

    /**
     * Overrides onCleared to add a log message
     */
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}