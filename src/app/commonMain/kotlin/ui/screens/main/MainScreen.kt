package ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import ui.components.map.MapControllers
import ui.components.map.MapView
import ui.components.map.rememberMapState
import utils.getScreenModel

data object MainScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MainScreenModel>()
        val state by screenModel.state.collectAsState(MainScreenState.Loading)
        val mapState = rememberMapState(ImageBitmap(100, 100), rotation = 180f)
        val blockColor = MaterialTheme.colorScheme.onSurface
        val clearColor = MaterialTheme.colorScheme.surface
        val unknownColor = MaterialTheme.colorScheme.surfaceContainerHighest
        Box(Modifier.fillMaxSize()) {
            when (state) {
                MainScreenState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Загрузка карты, подождите...")
                    }
                }

                is MainScreenState.Success -> {
                    LaunchedEffect(MaterialTheme.colorScheme.surface, state) {
                        val values = state as MainScreenState.Success
                        mapState.startRotation = -values.rotationAgrees.toFloat() - 90
                        mapState.resolution = values.resolution

                        mapState.reset()
                        mapState.bitmap =
                            screenModel.getBitmapWithColors(
                                values.byteArrayMap,
                                values.width,
                                values.height,
                                clearColor,
                                blockColor,
                                unknownColor
                            )

                    }



                    MapControllers(modifier = Modifier.fillMaxSize(), mapState){
                        MapView(Modifier.fillMaxSize().padding(), mapState)
                    }



                }

                MainScreenState.Error -> Text("Произошла ошибка при загрузке")
            }

        }
    }
}

