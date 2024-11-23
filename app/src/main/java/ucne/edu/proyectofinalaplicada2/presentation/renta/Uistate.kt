package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

data class Uistate(
    val rentaId: Int? = null,
    val clienteId: Int? = null,
    val vehiculoId: Int? = null,
    val fechaRenta: String = "",
    val fechaEntrega: String? = null,
    val total: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val rentas: List<RentaDto> = emptyList(),
    val vehiculos: List<VehiculoDto> = emptyList()
)

fun Uistate.toEntity() = RentaDto(
    rentaId = rentaId,
    clienteId = clienteId,
    vehiculoId = vehiculoId,
    fechaRenta = fechaRenta,
    fechaEntrega = fechaEntrega,
    total = total
)