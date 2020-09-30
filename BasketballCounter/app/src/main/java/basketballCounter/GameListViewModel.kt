package basketballCounter

import androidx.lifecycle.ViewModel
//import java.util.*

/**
 * GameListViewModel
 */
class GameListViewModel : ViewModel() {

    private val basketballGameRepository = BasketballGameRepository.get()

/*
    // un-comment this to add 150 random entries to the db
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') // list fo chars fro name generator

    //Create 150 games on initialization if there are none
    init {
        for (i in 1 until 151) {

            // Generate a seven character game name for teamA
            val teamAName = {
                (1..7)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }

            // Generate a seven character game name for teamB
            val teamBName = {
                (1..7)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }
            // generate a random score between 0 and 100 for both teams
            val teamAScore = (0..100).random()
            val teamBScore = (0..100).random()

            // Create a game object with the above variables and pass in the current calendar object
            val game = BasketballGame(
                UUID.randomUUID(), teamAScore, teamBScore,
                run(teamAName), run(teamBName), Calendar.getInstance().time
            )
            basketballGameRepository.addGame(game)
        }
    }
*/

    private val gameListLiveData = basketballGameRepository.getGames()

    /**
     * Getter for the list of games
     */
    val gameList
        get() = gameListLiveData
}