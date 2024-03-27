package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
expect fun Modifier.zoom(valueChanged: (Float) -> Unit): Modifier

// TODO: поворот на определённую клавишу или мышью
@Composable
expect fun Modifier.rotate(center: Offset, valueChanged: (Float) -> Unit): Modifier


