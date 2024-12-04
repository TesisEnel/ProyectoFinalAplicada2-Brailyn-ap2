package ucne.edu.proyectofinalaplicada2.presentation.authentication

import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto



data class ClienteUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val email: String = "",
    val password: String = "",
    val clienteId: Int? = null,
    val cedula: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val direccion: String = "",
    val celular: String = "",
    val clientes: List<ClienteEntity> = emptyList(),
    val errorNombre: String = "",
    val errorCedula: String = "",
    val errorApellidos: String = "",
    val errorDireccion: String = "",
    val errorCelular: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val clientebyEmail: ClienteDto? = null,
    val existCliente: Boolean = false,
    val isAdmin: Boolean = false,
    val isDataLoaded: Boolean = false
    )

fun ClienteUiState.toEntity() = ClienteDto(
    clienteId = clienteId,
    cedula = cedula,
    nombre = nombre,
    apellido = apellidos,
    direccion = direccion,
    celular = celular,
    email = email,
    isAdmin = isAdmin
)
