package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import basketballCounter.BasketballGame

/**
 * Database class for BasketballGames that calls the Dao
 */
@Database(entities = [ BasketballGame::class ], version=1)
@TypeConverters(GameTypeConverters::class)
abstract class BasketballGameDatabase : RoomDatabase() {

    abstract fun basketballGameDao(): BasketballGameDao
}