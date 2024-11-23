package ucne.edu.proyectofinalaplicada2.presentation.cliente

import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto

data class Uistate(
    val clienteId: Int? = null,
    val cedula: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val direccion: String = "",
    val celular: String = "",
    val clientes: List<ClienteDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
    val errorNombre: String = "",
    val errorCedula: String = "",
    val errorApellidos: String = "",
    val errorDireccion: String = "",
    val errorCelular: String = ""

)

fun Uistate.toEntity() = ClienteDto(
    clienteId = clienteId,
    cedula = cedula,
    nombre = nombre,
    apellido = apellidos,
    direccion = direccion,
    celular = celular
)
