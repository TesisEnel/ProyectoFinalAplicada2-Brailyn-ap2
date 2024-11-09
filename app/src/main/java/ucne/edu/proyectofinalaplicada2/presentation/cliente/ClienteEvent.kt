package ucne.edu.proyectofinalaplicada2.presentation.cliente

sealed interface ClienteEvent {
    data object Save : ClienteEvent
    data object Update : ClienteEvent
    data class OnchangeCedula(val cedula: String) : ClienteEvent
    data class OnchangeNombre(val nombre: String) : ClienteEvent
    data class OnchangeApellidos(val apellidos: String) : ClienteEvent
    data class OnchangeDireccion(val direccion: String) : ClienteEvent
    data class OnchangeCelular(val celular: String) : ClienteEvent
}