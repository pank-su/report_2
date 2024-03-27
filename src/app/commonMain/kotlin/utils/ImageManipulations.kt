package utils

import androidx.compose.ui.graphics.ImageBitmap

expect fun setPixels(pixels: ByteArray, width: Int, height: Int): ImageBitmap
