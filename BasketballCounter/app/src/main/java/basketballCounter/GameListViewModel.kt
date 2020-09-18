package basketballCounter

import androidx.lifecycle.ViewModel

/**
 * GameListViewModel
 */
class GameListViewModel : ViewModel() {

    private val games = mutableListOf<BasketballGame>()
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') // list fo chars fro name generator

    // Create 100 games on initialization
    init {
        for (i in 1 until 101) {

            // Generate a seven character game name for teamA
            val teamAName = {
                (1..7)
                    .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }

            // Generate a seven character game name for teamB
            val teamBName = {
                (1..7)
                    .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("");
            }
            // generate a random score between 0 and 100 for both teams
            val teamAScore = (0..100).random()
            val teamBScore = (0..100).random()

            // Generate unique game title
            val gameID = "Game #$i"

            // Create a game object with the above variables and pass in the current calendar object
            val game = BasketballGame(teamAScore, teamBScore,
                kotlin.run(teamAName), kotlin.run(teamBName), gameID, java.util.Calendar.getInstance())
            games += game
        }
    }

    /**
     * Getter for the list of games
     */
    val gameList
        get() = games
}