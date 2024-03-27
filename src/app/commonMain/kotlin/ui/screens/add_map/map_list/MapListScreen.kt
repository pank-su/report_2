package ui.screens.add_map.map_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import io.kamel.core.Resource
import io.kamel.core.isFailure
import io.kamel.core.isLoading
import io.kamel.core.isSuccess
import io.kamel.image.asyncPainterResource
import utils.getScreenModel

object MapListScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MapListScreenModel>()
        val state by screenModel.state.collectAsState(MapListState.Loading)
        if (state is MapListState.Error) Box(modifier = Modifier.fillMaxSize()) {
            Text("Ошибка, проверьте подключение к интернету")
            return
        }

        LazyVerticalGrid(
            GridCells.Adaptive(200.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {

            if (state is MapListState.Loading) {
                items(4) {
                    MapCard("", "", true)
                }
            } else if (state is MapListState.Successful) {
                items((state as MapListState.Successful).maps) {
                    MapCard(it.name, screenModel.imageUrl(it.name))
                }
            }
        }

    }

    @Composable
    private fun MapCard(name: String, imageUrl: String?, isLoading: Boolean = false) {
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
        val painter = asyncPainterResource(imageUrl ?: "")
        LaunchedEffect(painter){
            println(painter)
        }
        Card(onClick = {}) {

            AnimatedContent(painter.isLoading || isLoading) {

                Column {
                    if (painter.isLoading || isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest).shimmer(shimmerInstance))
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest).padding(12.dp).width(50.dp).height(20.dp))
                    } else if (painter.isSuccess && !isLoading) {
                        Image(
                            painter = (painter as Resource.Success).value,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                        Text(name, modifier = Modifier.padding(12.dp))
                    } else if (painter.isFailure && !isLoading){
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.surfaceContainerHighest).shimmer(shimmerInstance))
                        Text(name, modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}