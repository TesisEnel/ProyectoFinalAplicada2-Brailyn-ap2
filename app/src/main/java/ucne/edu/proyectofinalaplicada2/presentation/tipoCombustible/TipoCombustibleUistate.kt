package ucne.edu.proyectofinalaplicada2.presentation.tipoCombustible

import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity

data class TipoCombustibleUistate(
    val tipoCombustibles: List<TipoCombustibleEntity>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
