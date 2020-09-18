package basketballCounter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "GameListFragment"

/**
 * Fragment class that displays a randomly generated list of 100 Basketball game information
 */
class GameListFragment : Fragment() {
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    /**
     * Overrides the onCreate method to log when it was called
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        Log.d(TAG, "Total games: ${gameListViewModel.gameList.size}")
    }
    /**
     * Overrides the onCreateView method to set a recyclerView and add an adapter to it, so
     * that the view shows the list of game information
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        // update the UI by adding an adapter to populate the recycler view
        updateUI()

        Log.d(TAG, "onCreateView() called")

        return view
    }

    /**
     * adds the GameAdapter to the recycler view
     */
    private fun updateUI() {
        val games = gameListViewModel.gameList
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    /**
     * Companion object that creates a new instance of GameListFragment
     */
    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }

    /**
     * Inner class for the Game Holder that populates individual game list entries
     */
    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var game: BasketballGame

        // deceleration of UI elements that will be manipulated
        private val gameTitleTextView: TextView = itemView.findViewById(R.id.game_title)
        private val gameDateTextView: TextView = itemView.findViewById(R.id.game_date)
        private val gameVersusTextView: TextView = itemView.findViewById(R.id.game_versus)
        private val gameScoresTextView: TextView = itemView.findViewById(R.id.game_scores)

        // Binding function that builds the game list entry
        fun bind(game: BasketballGame) {
            this.game = game
            gameTitleTextView.text = this.game.gameName
            gameDateTextView.text = this.game.date.time.toString()
            gameVersusTextView.text =  "Team: " + this.game.teamAName + " vs. " + "Team: " + game.teamBName
            gameScoresTextView.text = this.game.teamAScore.toString() + " : " + game.teamBScore
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

        // gets the size of the list of games (100)
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