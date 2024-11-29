package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

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
    val rentas: List<RentaDto> = emptyList(),
    val vehiculoNombre: String? = null,
    val marca: MarcaEntity? = null,
    val vehiculo: VehiculoDto? = null,
    val cliente: ClienteDto? = null,
    val renta: RentaDto? = null
)
