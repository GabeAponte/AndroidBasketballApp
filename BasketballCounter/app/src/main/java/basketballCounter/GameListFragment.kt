package basketballCounter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "GameListFragment"
private const val ARG_WINNING_TEAM = "winningTeam"

/**
 * Fragment class that displays a randomly generated list of 100 Basketball game information
 */
class GameListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
        val getGameListViewModel : GameListViewModel
    }

    private var callbacks: Callbacks? = null
    private var currentWinner: String = ""
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter(emptyList())
    private var gameListViewModel: GameListViewModel? = null

    /**
     * Overrides the onCreate method to log when it was called and set the value
     * for the currentWinner argument that was passed from BasketballGameFragment and
     * display the list based off of it
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        if (arguments != null) {
            currentWinner = arguments?.getSerializable(ARG_WINNING_TEAM) as String
            Log.d(TAG, "args bundle currentWinner: $currentWinner")
        }
    }

    /**
     * Overrides the onAttach method to log when it was called and set callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        Log.d(TAG, "onAttach() called")
    }

    /**
     * Overrides the onDetach method to log when it was called and set callbacks
     */
    override fun onDetach() {
        super.onDetach()
        callbacks = null
        Log.d(TAG, "onDetach() called")
    }

    /**
     * Overrides the onCreateView method to set a recyclerView and add an adapter to it, so
     * that the view shows the list of game information
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        Log.d(TAG, "onCreateView() called")

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        gameRecyclerView.adapter = adapter

        // get the gameListViewModel from the main activity via the callback val
        gameListViewModel = callbacks!!.getGameListViewModel

        return view
    }

    /**
     * Override for the onViewCreated method that that observes the list of basketball games
     * from the GameListViewModel and displays each game in the UI based
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
        gameListViewModel?.gameList?.observe(
            viewLifecycleOwner,
            Observer { games ->
                games?.let {
                    Log.i(TAG, "Got BasketballGames: ${games.size}")
                    updateUI(games)
                }
            })
    }

    /**
     * Function that sorts the games from the GameListViewModel and then
     * adds the GameAdapter to the recycler view to show the games in the UI
     */
    private fun updateUI(games: List<BasketballGame>) {
        val gameList = sortGames(games) // sort the game based on the winning team passed in from BasketballGameFragment
        adapter = GameAdapter(gameList)
        gameRecyclerView.adapter = adapter
    }

    /**
     * Takes the list of games in the gameListViewModel and returns a new list based on which team is winning
     * All games are displayed displayed on a tie
     */
    private fun sortGames(games: List<BasketballGame>): List<BasketballGame> {
        val sortedGames = mutableListOf<BasketballGame>()
        for(game in games){
            when(currentWinner.trim()){
                "A" -> { if(game.teamAScore > game.teamBScore) sortedGames.add(game) }
                "B" -> { if(game.teamBScore > game.teamAScore) sortedGames.add(game) }
                "Tie" -> { return games }
                "" -> { return games }
            }
        }
        return sortedGames
    }

    /**
     * Companion object that creates a new instance of GameListFragment
     */
    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }

        // pass in the winningTeam to display only games where that team won in the list
        fun newInstance(winningTeam: String): GameListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WINNING_TEAM, winningTeam)
            }
            return GameListFragment().apply {
                arguments = args
            }
        }
    }

    /**
     * Inner class for the Game Holder that populates individual game list entries
     */
    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var game: BasketballGame

        // deceleration of UI elements that will be manipulated
        private val gameDateTextView: TextView = itemView.findViewById(R.id.game_date)
        private val gameVersusTextView: TextView = itemView.findViewById(R.id.game_versus)
        private val gameScoresTextView: TextView = itemView.findViewById(R.id.game_scores)
        private val logoImageView: ImageView = itemView.findViewById(R.id.logo_image_view)

        init {
            itemView.setOnClickListener(this)
        }

        // Binding function that builds the game list entry
        fun bind(game: BasketballGame) {
            this.game = game
            gameDateTextView.text = this.game.date.toString()
            gameVersusTextView.text = getString(R.string.Versus, game.teamAName, game.teamBName)
            gameScoresTextView.text = getString(R.string.Scores, game.teamAScore.toString(), game.teamBScore.toString())

            // set the logo based on who won the game and if there was a tie
            when {
                game.teamAScore > game.teamBScore -> logoImageView.setImageResource(R.drawable.ic_celticlogo)
                game.teamAScore < game.teamBScore -> logoImageView.setImageResource(R.drawable.ic_lakerlogo)
                else -> logoImageView.setImageResource(R.drawable.ic_tie)
            }
        }

        // onClick functionality for the GameListItems that switches to the BasketballGameFragment and passes the UUID
        override fun onClick(v: View?) {
            Log.d(TAG, "itemView.onClick() called")
            callbacks?.onGameSelected(game.id)
        }
    }

    /**
     * Inner class for the GameAdapter that populates the recycler view with all the game list entries
     */
    private inner class GameAdapter(var games: List<BasketballGame>) : RecyclerView.Adapter<GameHolder>() {

        /**
         * Overrides the onCreateViewHolder method to make a view holder for the Game List
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)

            Log.d(TAG, "onCreateViewHolder() called")

            return GameHolder(view)
        }

        // gets the size of the list of games
        override fun getItemCount() = games.size

        /**
         * Overrides the onBindViewHolder method that binds a game list entry to the view holder
         */
        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.apply {
                holder.bind(game)
            }
            Log.d(TAG, "onBindViewHolder() called")
        }
    }

    /**
     * Adding logs messages for onStart, onResume, onPause, onStop and onDestroy
     */
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