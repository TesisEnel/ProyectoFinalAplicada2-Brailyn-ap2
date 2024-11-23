package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import ucne.edu.proyectofinalaplicada2.data.remote.dto.TipoVehiculoDto

data class TipoVehiculoUistate(
    val tipoVehiculos: List<TipoVehiculoDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
