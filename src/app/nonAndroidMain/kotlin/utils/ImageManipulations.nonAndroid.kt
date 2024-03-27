package utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.*

actual fun setPixels(pixels: ByteArray, width: Int, height: Int): ImageBitmap {
    val bitmap = Bitmap()
    bitmap.setImageInfo(
        ImageInfo(
            width = width,
            height = height,
            colorInfo = ColorInfo(ColorType.RGBA_8888, alphaType = ColorAlphaType.UNPREMUL, colorSpace = null),
        )
    )
    bitmap.installPixels(pixels)
    return bitmap.asComposeImageBitmap()
}