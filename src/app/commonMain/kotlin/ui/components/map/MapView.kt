package ui.components.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import utils.rotate
import utils.zoom
import kotlin.math.PI


@Composable
fun MapView(modifier: Modifier = Modifier, mapState: MapState) {

    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        mapState.scale *= zoomChange
        mapState.scale = mapState.scale.coerceIn(mapState.minScale, mapState.maxScale)

        mapState.rotation += rotationChange

        val rotationInRad = (PI / 180.0) * mapState.rotation

        // так как у нас угол меняется мы должны посчитать изменение с учётом угла (https://en.wikipedia.org/wiki/Rotation_of_axes_in_two_dimensions)
        // Не нужно, так как теперь жесты распространяются на не движущееся поле
        /*val fixedOffsetChange = Offset(
            (offsetChange.x * cos(rotationInRad) - offsetChange.y * sin(rotationInRad)).toFloat(),
            (offsetChange.x * sin(rotationInRad) + offsetChange.y * cos(rotationInRad)).toFloat()
        )*/

        mapState.offset += offsetChange// * mapState.scale
    }



    Box(modifier = modifier.zoom {
        // TODO: добавить зум по курсору
        if (it == 1f && mapState.scale <= 0.11) return@zoom
        mapState.scale -= it * 0.1f
    }.rotate(mapState.offset + Offset(mapState.size.width / 2, mapState.size.height / 2)) {
        if (it.isNaN()) return@rotate
        mapState.rotation += it
    }.transformable(state).background(MaterialTheme.colorScheme.surfaceContainerHighest)) {


        Image(
            bitmap = mapState.bitmap,
            null,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                this.transformOrigin = TransformOrigin.Center
                scaleX = -mapState.scale
                scaleY = mapState.scale
                rotationZ = mapState.rotation
                mapState.size = this.size

                translationX = mapState.offset.x
                translationY = mapState.offset.y

            }, filterQuality = FilterQuality.None
        )
    }


}
