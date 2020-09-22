package basketballCounter

import android.app.Application
import android.util.Log

private const val TAG = "BasketballCounterApp"

/**
 * Application class for BasketballCounter that creates the BasketballGameRepository singleton
 */
class BasketballCounterApplication : Application() {

    /**
     * Override for the onCreate method to initialize the BasketballGameRepository singleton
     */
    override fun onCreate() {
        super.onCreate()
        BasketballGameRepository.initialize(this)
        Log.d(TAG, "onCreate() called")
        Log.d(TAG, "BasketballGameRepository initialized")
    }
}