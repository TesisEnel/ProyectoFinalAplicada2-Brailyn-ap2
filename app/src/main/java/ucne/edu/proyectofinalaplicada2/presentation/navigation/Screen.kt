package ucne.edu.proyectofinalaplicada2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data object VehiculoRegistroScreen: Screen()






    @Serializable
    data class VehiculePresentation(val id: Int): Screen()
}