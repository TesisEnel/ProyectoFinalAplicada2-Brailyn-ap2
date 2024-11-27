package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto


data class MarcaVehiculoUiState (
    val marcaId: Int? = null,
    val nombreMarca: String? = null,
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val vehiculos: List<VehiculoEntity> = emptyList()
)