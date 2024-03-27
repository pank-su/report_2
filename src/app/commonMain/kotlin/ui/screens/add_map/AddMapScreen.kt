package ui.screens.add_map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.screens.add_map.map_list.MapListScreen

object AddMapScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        //val navigator = LocalNavigator.currentOrThrow
        Navigator(MapListScreen){
            Scaffold(topBar = {
                TopAppBar(title = { Text("Выберите готовую карту или добавьте новую") })
            }){
                Box(modifier = Modifier.padding(it)){
                    CurrentScreen()

                }
            }
        }
    }
}