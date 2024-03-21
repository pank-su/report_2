package com.example.rostestapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rostestapplication.messages.main.Vector3
import com.example.rostestapplication.messages.main.test
import com.example.rostestapplication.ui.theme.RosTestApplicationTheme
import com.manalkaff.jetstick.JoyStick


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RosTestApplicationTheme {
                Column {
                    val viewModel: TurtleViewModel = viewModel()
                    TurtleView(modifier = Modifier.fillMaxWidth())
                    Row {

                        var msg by remember {
                            mutableStateOf(test(Vector3(0.0, 0.0, 0.0), Vector3(0.0, 0.0, 0.0)))
                        }
                        JoyStick(
                            Modifier.fillMaxWidth(0.5f),
                            dotSize = 30.dp
                        ) { x: Float, y: Float ->
                            msg.linear.x = y.toDouble() / 80.0
                            msg.linear.y = -x.toDouble() / 80.0
                            if (msg.linear.x != 0.0 && msg.linear.y != 0.0)

                            viewModel.sendPosition(msg)

                        }
                        JoyStick(
                            Modifier.fillMaxWidth(),

                            dotSize = 30.dp
                        ) { x: Float, y: Float ->
                            msg.angular.z = x.toDouble() / 100.0
                            if (msg.angular.z != 0.0)
                            viewModel.sendPosition(msg)

                        }



                    }

                }
            }
        }
    }
}

@Composable
fun TurtleView(modifier: Modifier = Modifier) {
    val viewModel: TurtleViewModel = viewModel()
    val color by viewModel.color.collectAsState()
    val turtlePosition by viewModel.turtlePosition.collectAsState()
    val rotation by viewModel.rotation.collectAsState()
    Box(modifier = modifier
        .aspectRatio(1f)
        .drawBehind {
            // 0.1 из черпахи будут делать сдвиг здесь
            val pixelsForSquare = size.height / 100
            val turtleSize = Size(pixelsForSquare * 3f, pixelsForSquare * 4f)
            val normalTurtlePosition = turtlePosition * 10f * pixelsForSquare - Offset(
                turtleSize.width / 2,
                turtleSize.height / 2
            )
            drawRect(color, Offset.Zero)
            rotate(rotation, turtlePosition * 10f * pixelsForSquare) {
                drawOval(
                    Color.Red,
                    normalTurtlePosition,
                    size = turtleSize,
                )
            }

        }) {
    }

}

@Preview
@Composable
private fun TurtleViewPreview() {
    TurtleView()
}


