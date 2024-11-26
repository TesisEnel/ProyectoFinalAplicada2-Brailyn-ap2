package ucne.edu.proyectofinalaplicada2.presentation.navigation

import androidx.navigation.NavBackStackEntry

interface MainEvent {
    data class UpdateCurrentRoute(val backStackEntry: NavBackStackEntry) : MainEvent
}