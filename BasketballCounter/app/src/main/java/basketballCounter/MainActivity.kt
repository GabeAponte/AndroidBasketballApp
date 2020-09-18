package basketballCounter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
private const val MAIN_FRAG_TAG = "MainFrag"
private const val LIST_FRAG_TAG = "ListFrag"

/**
 * Main activity class for hosting fragments
 */
class MainActivity : AppCompatActivity() {

    /**
     * Overrides the onCreate method to load fragments
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        // initialize both fragments and add them to the fragment_container,
        // but only show the main basketball game fragment
        if (currentFragment == null) {

            val mainFragment = BasketballGameFragment()
            val listFragment = GameListFragment.newInstance()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mainFragment, MAIN_FRAG_TAG)
                .add(R.id.fragment_container, listFragment, LIST_FRAG_TAG)
                .hide(listFragment)
                .commit();
        }

        /*if (currentFragment == null) {
            val fragment = GameListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }*/
    }

    /**
     * Overrides the onBackPressed() method so that users can return to the BasketballGameFragment when
     * pressing the back button on the GameListFragment. Note this does not save data so the BasketballGameFragment will be reset
     */
    override fun onBackPressed(){
        var count = supportFragmentManager.backStackEntryCount; // num of fragments in the backstack
        // if there are no fragments in the backstack, go back like normal
        if (count == 0) {
            super.onBackPressed();
        }
        // if there are fragments in the backstack, pop off the first one so that the last view is visible.
        else {
            supportFragmentManager.popBackStack();
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