package ucne.edu.proyectofinalaplicada2.presentation.proveedor

import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity

data class ProveedorUistate(
    val proveedorId: Int? = null,
    val proveedores: List<ProveedorEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
