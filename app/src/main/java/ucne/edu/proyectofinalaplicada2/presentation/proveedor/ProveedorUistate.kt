package ucne.edu.proyectofinalaplicada2.presentation.proveedor

import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ProveedorDto

data class ProveedorUistate(
    val proveedorId: Int? = null,
    val proveedores: List<ProveedorEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
    val nombre: String = "",
    val telefono: String = "",
    val errorNombre: String = "",
    val errorTelefono: String = "",
)

fun ProveedorUistate.toEntity() = ProveedorDto(
    proveedorId = proveedorId,
    nombre = nombre,
    celular = telefono,
    )