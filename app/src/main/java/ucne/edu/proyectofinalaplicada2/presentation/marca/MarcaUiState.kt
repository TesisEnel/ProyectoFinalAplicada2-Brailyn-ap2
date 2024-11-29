package ucne.edu.proyectofinalaplicada2.presentation.marca

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.MarcaVehiculoUiState

data class MarcaUiState(
    val marcaId: Int? = null,
    val nombreMarca: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val marcas: List<MarcaDto?> = emptyList(),
    val imageUrl: String? = null,
    val vehiculos: List<VehiculoEntity> = emptyList(),
    val marca: MarcaDto? = null
)
