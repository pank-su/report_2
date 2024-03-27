package domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import utils.setPixels

class SetImageColorsUseCase {

    operator fun invoke(
        data: ByteArray,
        width: Int,
        height: Int,
        clearColor: Color = Color.White,
        blockColor: Color = Color.Black,
        unknownColor: Color = Color(0xFFB4B3AE)
    ): ImageBitmap {
        val newByteArray = mutableListOf<Byte>()
        data.forEach {

            val color =
                if (it.toInt() == 100) blockColor else if (it.toInt() == -1) unknownColor else clearColor
            newByteArray.add((color.red * 0xFF).toInt().toByte())
            newByteArray.add((color.green * 0xFF).toInt().toByte())
            newByteArray.add((color.blue * 0xFF).toInt().toByte())
            newByteArray.add((color.alpha * 0xFF).toInt().toByte())
        }
        return setPixels(
            newByteArray.toByteArray(),
            width,
            height
        )
    }
}