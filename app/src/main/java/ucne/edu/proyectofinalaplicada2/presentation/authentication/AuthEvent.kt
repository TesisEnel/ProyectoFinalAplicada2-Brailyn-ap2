package ucne.edu.proyectofinalaplicada2.presentation.authentication

sealed interface AuthEvent {

    data class OnChangeCedula(val cedula : String) : AuthEvent
    data class OnChangeNombre(val nombre : String) : AuthEvent
    data class OnChangeApellidos(val apellidos : String) : AuthEvent
    data class OnChangeDireccion(val direccion : String) : AuthEvent
    data class OnChangeCelular(val celular : String) : AuthEvent
    data class OnChangeEmail(val email : String) : AuthEvent
    data class OnChangePassword(val password : String) : AuthEvent
    data object Login : AuthEvent
    data object Signup : AuthEvent
    data object Signout : AuthEvent

}