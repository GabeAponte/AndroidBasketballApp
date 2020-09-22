package basketballCounter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Data class for the BasketballGame object that holds the scores for each team, their name, the date of the game and a UUID
 */
@Entity
data class BasketballGame(@PrimaryKey val id: UUID = UUID.randomUUID(),
                          var teamAScore: Int, var teamBScore: Int, var teamAName: String, var teamBName: String,
                          var date: Date
)