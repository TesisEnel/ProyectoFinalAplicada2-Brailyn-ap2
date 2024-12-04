package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ModeloDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto

data class RentaUistate(
    val rentaId: Int? = null,
    val clienteId: Int? = null,
    val vehiculoId: Int? = null,
    val fechaRenta: String = "",
    val fechaEntrega: String? = null,
    val total: Double? = null,
    val cantidadDias: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val errorFechaRenta: String? = null,
    val errorFechaEntrega: String? = null,
    val success: String? = null,
    val rentas: List<RentaEntity> = emptyList(),
    val vehiculoNombre: String? = null,
    val vehiculoModelo: String? = null,
    val marca: MarcaEntity? = null,
    val modelo : ModeloEntity? = null,
    val tipoCombustibleEntity: TipoCombustibleEntity? = null,
    val vehiculoConCombustible: String? = null,
    val tipoVehiculoEntity: TipoVehiculoEntity? = null,
    val vehiculoConTipo: String? = null,
    val vehiculo: VehiculoEntity? = null,
    val cliente: ClienteDto? = null,
    val renta: RentaEntity? = null,
    val vehiculos: List<VehiculoEntity> = emptyList(),
    val modelos: List<ModeloDto> = emptyList(),
    val rentaConVehiculos: List<RentaConVehiculo> = emptyList(),
    val rentaConVehiculo: RentaConVehiculo? = null,
    val showModal: Boolean = false,
    val fechaValida: Boolean = false,
    val costoAdicional: Double = 0.0,
    val isDataLoading : Boolean = false,

    )

data class RentaConVehiculo(
    val nombreModelo: String? = null,
    val renta: RentaEntity? = null,
    val marca: MarcaEntity? = null,
    val anio: Int? = null,
    val vehiculoEntity: VehiculoEntity? = null,
    val clienteEntity: ClienteEntity? = null
)

fun RentaUistate.toDto() = RentaDto(
    rentaId = rentaId,
    clienteId = clienteId,
    vehiculoId = vehiculoId,
    fechaRenta = fechaRenta,
    fechaEntrega = fechaEntrega,
    total = total
)
