package ucne.edu.proyectofinalaplicada2.presentation.authentication

sealed interface AuthEvent {
    data class OnChangeEmail(val email : String) : AuthEvent
    data class OnChangePassword(val password : String) : AuthEvent
    data object Login : AuthEvent
    data object Signup : AuthEvent
    data object Signout : AuthEvent


}