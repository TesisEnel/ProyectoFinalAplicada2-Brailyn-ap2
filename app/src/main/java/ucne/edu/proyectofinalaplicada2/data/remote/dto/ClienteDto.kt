package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class ClienteDto(
    val clienteId: Int?,
    val cedula: String?,
    val nombre: String?,
    val apellidos: String?,
    val direccion: String?,
    val celular: String?
)