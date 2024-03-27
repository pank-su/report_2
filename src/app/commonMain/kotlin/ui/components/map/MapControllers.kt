package ui.components.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.screens.add_map.AddMapScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalFoundationApi::class)
@Composable
fun MapControllers(modifier: Modifier, mapState: MapState, content: @Composable () -> Unit) {
    val windowSizeClass = calculateWindowSizeClass()
    val density = LocalDensity.current
    var isEditable by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    val linage = movableContentOf {
        Spacer(modifier = Modifier.size(4.dp))
        Box(Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(12.dp))) {
            if (!isEditable) {
                Text("${mapState.pxOnMetre * with(density) {mapState.linageSize.toPx()}} m", modifier = Modifier.padding(2.dp).pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        isEditable = true
                    })
                })
            } else {
                var meters by remember {
                    mutableStateOf(mapState.pxOnMetre * with(density) {mapState.linageSize.toPx()})
                }
                BasicTextField(
                    value = meters.toString(),
                    onValueChange = {
                        meters = it.toFloatOrNull() ?: return@BasicTextField
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        mapState.setScaleByMeters(meters)
                        isEditable = false
                    }, ), modifier = Modifier.onKeyEvent {
                        if (it.key == Key.Enter){
                            mapState.setScaleByMeters(meters)
                          isEditable = false

                            return@onKeyEvent true
                        }
                        false
                    }
                )
            }
        }
    }



    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            Box(modifier = modifier) {
                content()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterStart).padding(24.dp)
                ) {
                    VerticalDivider(
                        Modifier.height(mapState.linageSize),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    linage()
                }
                Column(
                    Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier)
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        OutlinedIconButton(
                            onClick = {
                                mapState.plusScale()
                            },
                            colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = MaterialTheme.colorScheme.background)
                        ) {
                            Icon(Icons.Default.Add, "scale")
                        }
                        OutlinedIconButton(
                            onClick = {
                                mapState.minusScale()

                            },
                            colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = MaterialTheme.colorScheme.background),

                            ) {
                            Icon(Icons.Default.Remove, "unscale")
                        }
                    }
                    Column {
                        AnimatedVisibility(mapState.scale != mapState.startScale || mapState.startOffset != mapState.offset || mapState.startRotation != mapState.rotation) {
                            IconButton(onClick = {
                                mapState.reset()
                            }) {
                                Icon(Icons.Default.Sync, "reset map view")
                            }
                        }
                        FloatingActionButton(
                            onClick = { navigator.push(AddMapScreen)  },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.AddToPhotos, "add new map")
                        }

                    }

                }
            }
        }

        else -> {
            Scaffold(modifier, bottomBar = {
                BottomAppBar(actions = {
                    IconButton(onClick = {
                        mapState.minusScale()
                    }) {
                        Icon(Icons.Default.Remove, "unscale")
                    }
                    IconButton(onClick = {
                        mapState.plusScale()
                    }) {
                        Icon(Icons.Default.Add, "scale")
                    }
                    IconButton(onClick = {
                        mapState.reset()
                    }) {
                        Icon(Icons.Default.Sync, "reset map view")

                    }
                }, floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navigator.push(AddMapScreen)  },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.AddToPhotos, "add new map")
                    }

                })
            }) {
                Box(modifier = Modifier.padding(it)) {

                    content()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        HorizontalDivider(
                            Modifier.width(mapState.linageSize),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        linage()
                    }
                }

            }
        }
    }
}