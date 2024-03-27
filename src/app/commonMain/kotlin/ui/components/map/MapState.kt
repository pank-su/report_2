package ui.components.map

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

class MapState(
    bitmap: ImageBitmap,
    val startScale: Float,
    var startRotation: Float, val startOffset: Offset,
    val density: Density
) {

    var bitmap by mutableStateOf(bitmap)
    var scale by mutableStateOf(startScale)
    var rotation by mutableStateOf(startRotation)
    var offset by mutableStateOf(startOffset)
    var size by mutableStateOf(Size.Zero)
    val maxScale = 20f
    val minScale = 0.1f

    var resolution: Float by mutableStateOf(1f)
    val linageSize = 200.dp


    val pxOnMetre: Float
        get() {
            val imagePixelInGui = bitmap.width / size.width
            return with(density) {((imagePixelInGui * resolution) / scale)}
        }


    fun plusScale(){
        scale += 0.1f
        scale = scale.coerceIn(minScale..maxScale)
    }

    fun minusScale(){
        scale -= 0.1f
        scale = scale.coerceIn(minScale..maxScale)
    }

    fun setScaleByMeters(meters: Float){
        val imagePixelInGui = bitmap.width / size.width

        scale = with(density) {(imagePixelInGui * resolution) / (meters / linageSize.toPx() )}
    }


    fun reset() {
        scale = startScale
        rotation = startRotation
        offset = startOffset
    }

    companion object {
        /*val Saver: Saver<MapState, ImageBitmap> = Saver(
            save = { MapState() },
            restore = { MapState(it) }
        )*/
    }


}

// TODO: make savable
@Composable
fun rememberMapState(bitmap: ImageBitmap, scale: Float = 1f, rotation: Float = 0f, offset: Offset = Offset.Zero, density: Density = LocalDensity.current) =
    remember {
        MapState(bitmap, scale, rotation, offset, density)
    }
