package basketballCounter

import java.util.*

/**
 * Data class for the BasketballGame object that holds the scores for each team
 */
data class BasketballGame(
    var teamAScore: Int, var teamBScore: Int, var teamAName: String, var teamBName: String,
    var gameName: String, var date: Calendar
)