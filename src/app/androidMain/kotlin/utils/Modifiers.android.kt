package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
actual fun Modifier.zoom(valueChanged: (Float) -> Unit): Modifier {
    return this
}

@Composable
actual fun Modifier.rotate(center: Offset, valueChanged: (Float) -> Unit): Modifier {
    return this
}