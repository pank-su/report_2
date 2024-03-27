package ui.di

import org.koin.dsl.module
import ui.screens.add_map.map_list.MapListScreenModel
import ui.screens.main.MainScreenModel

val uiModule = module {
    factory {
        MainScreenModel(get(), get())
    }
    factory {
        MapListScreenModel(get())
    }
}