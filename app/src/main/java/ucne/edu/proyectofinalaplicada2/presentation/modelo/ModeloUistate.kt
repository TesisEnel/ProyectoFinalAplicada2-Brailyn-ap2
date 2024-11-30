package ucne.edu.proyectofinalaplicada2.presentation.modelo

import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity

data class ModeloUistate(
    val modeloId: Int? = null,
    val modelos: List<ModeloEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
