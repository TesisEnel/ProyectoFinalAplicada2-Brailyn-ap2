package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity

data class TipoVehiculoUistate(
    val tipoVehiculos: List<TipoVehiculoEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
)
