package basketballCounter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "MainActivity"

/**
 * Main activity class for hosting fragments
 */
class MainActivity : AppCompatActivity(), BasketballGameFragment.Callbacks, GameListFragment.Callbacks {

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    /**
     * Overrides the onCreate method to load fragments
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = BasketballGameFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    /**
     * Override for the onDisplayClicked method that switches to the GameListFragment
     * and sends the winningTeam to it
     */
    override fun onDisplayClicked(winningTeam : String) {
        Log.d(TAG, "onDisplayClicked() called with Winning Team: $winningTeam")
        val fragment = GameListFragment.newInstance(winningTeam)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Override for the onGameSelected method that switches to the BasketballGameFragment
     * and sends the selected GameID to it so that the game data is displayed
     */
    override fun onGameSelected(gameId: UUID) {
        Log.d(TAG, "onGameSelected() called with Game ID: $gameId")
        val fragment = BasketballGameFragment.newInstance(gameId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Override for the getGameListViewModel val that get the value of
     * gameListViewModel
     */
    override val getGameListViewModel: GameListViewModel
        get() = gameListViewModel


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