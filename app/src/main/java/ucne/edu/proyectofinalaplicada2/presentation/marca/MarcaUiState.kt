package ucne.edu.proyectofinalaplicada2.presentation.marca

import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.MarcaVehiculoUiState

data class MarcaUiState(
    val marcaId: Int? = null,
    val nombreMarca: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val marcas: List<MarcaDto> = emptyList(),
    val marcaVehiculos: List<MarcaVehiculoUiState> = emptyList(),
)
