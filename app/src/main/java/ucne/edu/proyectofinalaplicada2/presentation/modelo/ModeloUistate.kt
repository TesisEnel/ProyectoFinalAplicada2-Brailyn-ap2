package ucne.edu.proyectofinalaplicada2.presentation.modelo

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity

data class ModeloUistate(
    val modeloId: Int? = null,
    val modelos: List<ModeloEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = "",
    val vehiculos: List<VehiculoEntity> = emptyList(),
    val modeloConVehiculos: List<ModeloConVehiculo> = emptyList(),
    val isDataLoaded: Boolean = false,
    val marca: MarcaEntity? = null
)

data class ModeloConVehiculo(
    val nombreModelo: String,
    val vehiculo: VehiculoEntity,
    val marca: MarcaEntity?
)
