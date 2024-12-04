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

        val currentRoute = Screen.fromRoute(
            route = backStackEntryRoute.destination.route ?: "",
            args = backStackEntryRoute.arguments
        )

        val (title, showBackButton) = when (currentRoute) {
            is Screen.VehiculoRegistroScreen -> "Registro de Vehículo" to false
            is Screen.RentaScreen -> "Renta de Vehículo" to true
            is Screen.TipoVehiculoListScreen -> "Puedes elegir tu vehículo" to true
            is Screen.AuthScreen -> "Autenticación" to false
            is Screen.RegistroClienteScreen -> "Registro de Cliente" to false
            null -> "BravquezRentcar" to false
            is Screen.Home -> "Bienvenido" to false
            Screen.RentaListScreen -> "Rentas" to false
            Screen.FiltraVehiculo -> "Puedes buscar tu vehículo" to true
            Screen.Settings -> "Configura tu perfil" to false
            Screen.ProveedorRegistroScreen -> "Agregar Proveedor" to false
        }

        _uiState.update {
            it.copy(
                currentTitle = title,
                showBackButton = showBackButton,
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