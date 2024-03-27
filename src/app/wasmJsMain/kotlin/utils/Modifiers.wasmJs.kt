package utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.onPointerEvent
import kotlin.math.PI
import kotlin.math.atan2


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.zoom(valueChanged: (Float) -> Unit): Modifier {
    return this then Modifier.onPointerEvent(PointerEventType.Scroll) {

        valueChanged(if (it.changes.first().scrollDelta.y > 0) 1f else -1f)
    }
}

// TODO: не работает
@Composable
@OptIn(ExperimentalFoundationApi::class)
actual fun Modifier.rotate(center: Offset, valueChanged: (Float) -> Unit): Modifier {
    var startPosition = Offset.Zero

    return this then Modifier.onDrag(
        true,
        PointerMatcher.pointer(PointerType.Mouse, PointerButton.Secondary),
        onDragStart = { startPosition = it }) {
//        println(center)
//        val distanceA = (startPosition - center).getDistance()
//        startPosition += it
//        val distanceB = (startPosition - center).getDistance()
//        val distanceC = it.getDistance()
//        println(startPosition)

        val startAngle = atan2(startPosition.y - center.y, startPosition.x - center.x)
        startPosition += it
        val pastAngle = atan2(startPosition.y - center.y, startPosition.x - center.x)



        valueChanged(-((startAngle - pastAngle) * 180f / PI).toFloat())
        startPosition += it
    }
}


