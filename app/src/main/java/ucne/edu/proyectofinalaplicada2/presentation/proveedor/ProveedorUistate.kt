package ucne.edu.proyectofinalaplicada2.presentation.proveedor

import ucne.edu.proyectofinalaplicada2.data.remote.dto.ProveedorDto

data class ProveedorUistate(
    val proveedorId: Int? = null,
    val proveedores: List<ProveedorDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
