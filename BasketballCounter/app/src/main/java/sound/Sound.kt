package sound

private const val WAV = ".wav"

/**
 * Sound class that contains the asset file path and id for each sound
 */
class Sound(val assetPath: String, var soundId: Int? = null) {

    val name = assetPath.split("/").last().removeSuffix(WAV)
}