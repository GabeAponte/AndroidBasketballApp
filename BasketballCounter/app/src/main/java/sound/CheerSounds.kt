package sound

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

private const val TAG = "CheerSounds"
private const val SOUNDS_FOLDER = "cheering_sounds"
private const val MAX_SOUNDS = 2

/**
 * CheerSound class that contains functionality to load and play sounds from a SoundPool
 */
class CheerSounds (private val assets: AssetManager) {
    val sounds: List<Sound>

    // SoundPool initialization
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()

    // loads the sounds on creation
    init {
        sounds = loadSounds()
    }

    /**
     * Function that plays a sound based off which sound is passed in
     */
    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    /**
     * Function to release the SoundPool when we are done with it
     */
    fun release() {
        soundPool.release()
    }

    /**
     * Function that loads all the sounds that are stored in the assets folder
     */
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>

        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch (ioe: IOException) {
                Log.e(TAG, "Could not load sound $filename", ioe)
            }
        }
        return sounds
    }

    /**
     * Function that loads a sound with the AssetFileDescriptor
     */
    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }
}