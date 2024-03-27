package ui.screens.main

import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.ScreenModel
import data.MapLoaderRepository
import domain.SetImageColorsUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.math.PI
import kotlin.math.atan2


class MainScreenModel(
    private val mapLoaderRepository: MapLoaderRepository,
    val setImageColorsUseCase: SetImageColorsUseCase
) : ScreenModel {
    var state = flow {
        mapLoaderRepository.currentMap().map {
            if (it?.first == null) return@map MainScreenState.Loading
            val grid = it.first!!

            val sinyCosp: Double = 2 * (grid.info.origin.orientation.w * grid.info.origin.orientation.z)
            val cosyCosp: Double = 1 - 2 * (grid.info.origin.orientation.z * grid.info.origin.orientation.z)
            val yaw = atan2(sinyCosp, cosyCosp)
            return@map MainScreenState.Success(
                it.first!!.data.toByteArray(),
                grid.info.width.toInt(),
                grid.info.height.toInt(),
                grid.info.resolution,
                yaw * (180 / PI)
            )
        }.catch { MainScreenState.Error }.collect(::emit)
    }

    fun getBitmapWithColors(
        byteArray: ByteArray, width: Int, height: Int, clearColor: Color = Color.White,
        blockColor: Color = Color.Black,
        unknownColor: Color = Color(0xFFB4B3AE)
    ) = setImageColorsUseCase(byteArray, width, height, clearColor, blockColor, unknownColor)

}