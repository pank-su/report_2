package data

import com.github.thoebert.krosbridge.Ros
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object RosClient {
    val ros = Ros("192.168.0.105", 8080)
}