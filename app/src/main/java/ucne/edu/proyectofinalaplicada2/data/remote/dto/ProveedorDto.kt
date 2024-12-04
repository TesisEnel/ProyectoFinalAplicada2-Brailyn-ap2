package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity

data class ProveedorDto(
    val proveedorId: Int?,
    val nombre: String,
    val celular: String,
)

fun ProveedorDto.toEntity()= ProveedorEntity(
    proveedorId = proveedorId,
    nombre = nombre,
    celular = celular
)
