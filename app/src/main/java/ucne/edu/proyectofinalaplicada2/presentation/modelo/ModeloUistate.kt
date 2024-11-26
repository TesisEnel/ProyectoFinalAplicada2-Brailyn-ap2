package ucne.edu.proyectofinalaplicada2.presentation.modelo

import ucne.edu.proyectofinalaplicada2.data.remote.dto.ModeloDto

data class ModeloUistate(
    val modeloId: Int? = null,
    val modelos: List<ModeloDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
