package data

import com.github.thoebert.krosbridge.Ros
import kotlinx.coroutines.flow.Flow
import su.pank.rmsui.messages.nav_msgs.msg.OccupancyGrid
import su.pank.rmsui.messages.nav_msgs.msg.OccupancyGridTopic
import kotlin.random.Random

class MapLoaderRepository(val ros: Ros) {

    private var _currentMap: Flow<Pair<OccupancyGrid?, String?>?>? = null

    suspend fun currentMap(): Flow<Pair<OccupancyGrid?, String?>?> {
        while (!ros.isConnected) ros.connect()
        if (_currentMap == null) _currentMap = OccupancyGridTopic(ros, "/map").subscribe(Random.nextInt())
        return _currentMap!!
    }

}