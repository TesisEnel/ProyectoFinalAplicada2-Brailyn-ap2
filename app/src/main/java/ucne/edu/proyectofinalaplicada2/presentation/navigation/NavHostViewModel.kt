package ucne.edu.proyectofinalaplicada2.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
): ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()



    private fun updateCurrentRoute(backStackEntryRoute: NavBackStackEntry) {
        val user = firebaseAuth.currentUser

        val currentRoute = Screen.fromRoute(
            route = backStackEntryRoute.destination.route ?: "",
            args = backStackEntryRoute.arguments
        )

        val title = when (currentRoute) {
            is Screen.VehiculoRegistroScreen -> "Registro de Vehículo"
            is Screen.RentaScreen -> "Renta de Vehículo"
            is Screen.TipoVehiculoListScreen -> "Puedes elegir tu vehículo"
            is Screen.AuthScreen -> "Autenticación"
            is Screen.RegistroClienteScreen -> "Registro de Cliente"
            null -> "BravquezRentcar"
            is Screen.Home -> "Bienvenido, ${user?.displayName}"
            Screen.RentaListScreen -> "Rentas"
        }

        _uiState.update {
            it.copy(
                currentTitle = title,
                userDisplayName = firebaseAuth.currentUser?.displayName,
                userPhotoUrl = firebaseAuth.currentUser?.photoUrl.toString()
            )
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.UpdateCurrentRoute -> updateCurrentRoute(event.backStackEntry)
        }
    }

}