package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ModeloDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoConMarca

data class RentaUistate(
    val rentaId: Int? = null,
    val clienteId: Int? = null,
    val vehiculoId: Int? = null,
    val fechaRenta: String = "",
    val fechaEntrega: String? = null,
    val total: Double? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val rentas: List<RentaEntity> = emptyList(),
    val vehiculoNombre: String? = null,
    val marca: MarcaEntity? = null,
    val vehiculo: VehiculoEntity? = null,
    val cliente: ClienteDto? = null,
    val renta: RentaDto? = null,
    val vehiculos: List<VehiculoEntity> = emptyList(),
    val modelos: List<ModeloDto> = emptyList(),
    val rentaConVehiculo: List<RentaConVehiculo> = emptyList(),

    )

data class RentaConVehiculo(
    val nombreModelo: String? = null,
    val renta: RentaEntity? = null,
    val marca: MarcaEntity? = null,
    val anio: Int? = null,
)


