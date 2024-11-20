package ucne.edu.proyectofinalaplicada2.presentation.authentication

import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto

data class UiState(
    val clienteId: Int? = null,
    val cedula: String? = null,
    val nombre: String? = null,
    val apellidos: String? = null,

    val direccion: String? = null,
    val celular: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val email: String = "",
    val password: String = "",
    val clientes: List<ClienteDto> = emptyList()
)

fun UiState.toEntity() = ClienteDto(
    clienteId = clienteId,
    cedula = cedula,
    nombre = nombre,
    apellido = apellidos,
    direccion = direccion,
    celular = celular
)