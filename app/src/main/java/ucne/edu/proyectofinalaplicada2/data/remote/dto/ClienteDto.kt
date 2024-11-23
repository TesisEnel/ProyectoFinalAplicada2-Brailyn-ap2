package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class ClienteDto(
    val clienteId: Int?,
    val cedula: String?,
    val nombre: String?,
    val apellido: String?,
    val direccion: String?,
    val celular: String?,
    val email: String?
)