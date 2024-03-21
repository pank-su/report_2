package com.example.rostestapplication

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.rostestapplication.messages.main.test
import com.example.rostestapplication.messages.main.testTopic
import com.example.rostestapplication.messages.turtlesim.PoseTopic
import com.github.thoebert.krosbridge.Ros
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TurtleViewModel: ViewModel() {
    private val _color = MutableStateFlow(Color.Cyan)
    val color = _color.asStateFlow()

    private val _turtlePosition = MutableStateFlow(Offset(5f,5f))
    val turtlePosition = _turtlePosition.asStateFlow()

    private val _rotation = MutableStateFlow(0f)
    val rotation = _rotation.asStateFlow()
    lateinit var ros: Ros
    lateinit var testTopic: testTopic

    init {
        CoroutineScope(Dispatchers.IO).launch {
            ros = Ros("192.168.0.105", 8080)
            ros.connect()
            testTopic = testTopic(ros, "/turtle1/cmd_vel")

            PoseTopic(ros, "/turtle1/pose").subscribe("Test"){ msg, _ ->
                _turtlePosition.value = Offset(msg.x, 10 -msg.y)
                _rotation.value = -msg.theta * 57.2958f - 90
            }

        }
    }

    fun sendPosition(msg: test){
        testTopic.publish(msg)
    }

}