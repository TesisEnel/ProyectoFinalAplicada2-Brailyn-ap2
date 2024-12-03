package ucne.edu.proyectofinalaplicada2.presentation.authentication



sealed interface AuthEvent {
    data class OnChangeEmail(val email : String) : AuthEvent
    data class OnChangePassword(val password : String) : AuthEvent
    data object Login : AuthEvent
    data object Signup : AuthEvent
    data object Signout : AuthEvent
    data object SignInWithGoogle : AuthEvent
    data class OnchangeCedula(val cedula: String) : AuthEvent
    data class OnchangeNombre(val nombre: String) : AuthEvent
    data class OnchangeApellidos(val apellidos: String) : AuthEvent
    data class OnchangeDireccion(val direccion: String) : AuthEvent
    data class OnchangeCelular(val celular: String) : AuthEvent
    data object SaveCliente: AuthEvent
    data class UpdateUsuario(val email: String): AuthEvent
    data object UpdateClient: AuthEvent
    data object ClearData: AuthEvent
    data object ClearError: AuthEvent

}