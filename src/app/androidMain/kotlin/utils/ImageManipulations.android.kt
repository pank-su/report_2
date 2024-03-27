package utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb

actual fun setPixels(pixels: ByteArray, width: Int, height: Int): ImageBitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val intArray = IntArray(pixels.size / 4)
    for (i in 0..pixels.lastIndex / 4){
        intArray[i] = Color(pixels[i * 4].toInt(), pixels[i * 4 + 1].toInt(), pixels[i * 4 + 2].toInt(), pixels[i * 4 + 3].toInt()).toArgb()
    }
    bitmap.setPixels(intArray, 0, width, 0, 0, width, height)
    return bitmap.asImageBitmap()
}