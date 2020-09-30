package basketballCounter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

private const val TAG =  "GameDetailViewModel"

/**
 * ViewModel class that grabs LiveData from the basketball-game database and also saves new data to it
 */
class GameDetailViewModel: ViewModel() {

    private val basketballGameRepository = BasketballGameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()
    var gameLiveData: LiveData<BasketballGame?> = Transformations.switchMap(gameIdLiveData) {
            gameId -> basketballGameRepository.getGame(gameId)
    }

    /**
     * Function to load a game from teh database based on the inputted UUID
     */
    fun loadGame(gameId: UUID) {
        Log.d(TAG, "loadGame() called")
        gameIdLiveData.value = gameId
    }

    /**
     * Function that saves the inputted basketball game to the database
     */
    fun saveGame(game: BasketballGame) {
        Log.d(TAG, "saveGame() called")
        basketballGameRepository.updateGame(game)
    }

    /**
     * Function for getting team A's image file
     */
    fun getTeamAPhotoFile(game: BasketballGame): File {
        return basketballGameRepository.getTeamAPhotoFile(game)
    }

    /**
     * Function for getting team B's image file
     */
    fun getTeamBPhotoFile(game: BasketballGame): File {
        return basketballGameRepository.getTeamBPhotoFile(game)
    }
}