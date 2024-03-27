package ui.screens.main

sealed interface MainScreenState {
    object Loading : MainScreenState
    data class Success(val byteArrayMap: ByteArray, val width: Int, val height: Int, val resolution: Float, val rotationAgrees: Double): MainScreenState

    object Error : MainScreenState
}