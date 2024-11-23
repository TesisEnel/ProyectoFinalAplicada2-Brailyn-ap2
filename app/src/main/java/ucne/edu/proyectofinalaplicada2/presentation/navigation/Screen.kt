package ucne.edu.proyectofinalaplicada2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data object VehiculoRegistroScreen: Screen()
    @Serializable
    data object AuthScreen: Screen()
    @Serializable
    data class RentaScreen(val id: Int): Screen()
    @Serializable
    data class TipoVehiculoListScreen(val id: Int): Screen()
}