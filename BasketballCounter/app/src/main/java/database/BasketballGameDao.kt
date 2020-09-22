package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import basketballCounter.BasketballGame
import java.util.*

/**
 * Dao interface for the basketball game database
 */
@Dao
interface BasketballGameDao {

    /**
     * Gets all the data from the db and sorts it by the latest entries/updates
     */
    @Query("SELECT * FROM basketballgame ORDER BY date DESC")
    fun getGames(): LiveData<List<BasketballGame>>

    /**
     * Gets the game from the db that is associated with the UUID
     */
    @Query("SELECT * FROM basketballgame WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<BasketballGame?>

    /**
     * Updates a game in the db based on its UUID
     */
    @Update
    fun updateGame(game: BasketballGame)

    /**
     * Adds a new game to the db
     */
    @Insert
    fun addGame(game: BasketballGame)
}