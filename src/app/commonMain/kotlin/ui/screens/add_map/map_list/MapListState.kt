package ui.screens.add_map.map_list

import data.model.MapDTO

sealed interface MapListState {
    data object Loading: MapListState
    data class Successful(val maps: List<MapDTO>): MapListState

    data object Error : MapListState
}