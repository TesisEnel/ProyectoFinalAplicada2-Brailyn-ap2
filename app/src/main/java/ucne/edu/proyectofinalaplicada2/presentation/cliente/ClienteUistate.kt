package ucne.edu.proyectofinalaplicada2.presentation.cliente

import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto

data class ClienteUistate(
    val clienteId: Int? = null,
    val cedula: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val direccion: String = "",
    val celular: String = "",
    val clientes: List<ClienteDto> = emptyList(),
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
    val errorNombre: String = "",
    val errorCedula: String = "",
    val errorApellidos: String = "",
    val errorDireccion: String = "",
    val errorCelular: String = "",
    val errorEmail: String = "",

)

fun ClienteUistate.toEntity() = ClienteDto(
    clienteId = clienteId,
    cedula = cedula,
    nombre = nombre,
    apellido = apellidos,
    direccion = direccion,
    celular = celular,
    email = email
)
