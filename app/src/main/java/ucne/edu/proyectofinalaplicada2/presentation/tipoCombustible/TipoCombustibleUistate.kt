package ucne.edu.proyectofinalaplicada2.presentation.tipoCombustible

import ucne.edu.proyectofinalaplicada2.data.remote.dto.TipoCombustibleDto

data class TipoCombustibleUistate(
    val tipoCombustibles: List<TipoCombustibleDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
