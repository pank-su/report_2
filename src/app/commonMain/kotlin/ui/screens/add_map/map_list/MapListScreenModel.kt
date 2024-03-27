package ui.screens.add_map.map_list

import cafe.adriel.voyager.core.model.ScreenModel
import data.MapHelperRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class MapListScreenModel(val mapHelperRepository: MapHelperRepository) : ScreenModel {

    val state = flow<MapListState> {
        while (true) {
            try {
                emit(MapListState.Successful(mapHelperRepository.getMaps()))

            } catch (e: Exception) {
                e.printStackTrace()
                emit(MapListState.Error)
            }
            delay(1.minutes)
        }
    }

    fun imageUrl(mapName: String) = "${mapHelperRepository.basicUrl}/image/$mapName"

}