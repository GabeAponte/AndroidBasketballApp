package basketballCounter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import androidx.exifinterface.media.ExifInterface
import java.io.File
import kotlin.math.roundToInt

/**
 * Function that get the scaled bitmap
 */
fun getScaledBitmap(path: String, activity: Activity): Bitmap {
    val size = Point()
    activity.windowManager.defaultDisplay.getSize(size)

    return getScaledBitmap(path, size.x, size.y)
}

fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    // Read in the dimensions of the image on disk
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    // Figure out how much to scale down by
    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWidth

        val sampleScale = if (heightScale > widthScale) {
            heightScale
        } else {
            widthScale
        }
        inSampleSize = sampleScale.roundToInt()
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    // Read in and create final bitmap
    return BitmapFactory.decodeFile(path, options)
}

/**
 * Function that uses ExifInterface to rotate pictures to the correct orientation and return the new bitmap
 */
fun rotateImage(bitmap: Bitmap, path: File?): Bitmap? {

    val exifInterface = ExifInterface(path!!.absolutePath)
    val orientation: Int = exifInterface.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    val matrix = Matrix()

    // handles all the orientation cases for any device
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
           matrix.setRotate(90F)
        }
        ExifInterface.ORIENTATION_ROTATE_180 -> {
            matrix.setRotate(180F)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> {
            matrix.setRotate(270F)
        }
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
            matrix.setScale(-1F, 1F)
        }
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            matrix.setRotate(180F)
            matrix.setScale(-1F, 1F)
        }
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.setRotate(90F)
            matrix.setScale(-1F, 1F)
        }
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.setRotate(-90F)
            matrix.setScale(-1F, 1F)
        }
    }
    // create the rotated bitmap
    return try {
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}