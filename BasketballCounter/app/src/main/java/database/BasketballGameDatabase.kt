package basketballCounter

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ BasketballGame::class ], version=1)
@TypeConverters(GameTypeConverters::class)
abstract class BasketballGameDatabase : RoomDatabase() {
}