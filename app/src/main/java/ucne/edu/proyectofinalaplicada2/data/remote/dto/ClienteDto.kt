package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity

data class ClienteDto(
    val clienteId: Int?,
    val cedula: String?,
    val nombre: String?,
    val apellido: String?,
    val direccion: String?,
    val celular: String?,
    val email: String?,
    val isAdmin: Boolean?,
)

fun ClienteDto.toEntity() = ClienteEntity(
    clienteId = clienteId,
    cedula = cedula,
    nombre = nombre,
    apellido = apellido,
    direccion = direccion,
    celular = celular,
    email = email,
    isAdmin = isAdmin,
)