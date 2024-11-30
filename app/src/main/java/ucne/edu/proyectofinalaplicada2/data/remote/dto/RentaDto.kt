package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity

data class RentaDto(
    val rentaId: Int? = null,
    val clienteId: Int?,
    val vehiculoId: Int?,
    val fechaRenta: String?,
    val fechaEntrega: String?,
    val total: Double?
)

fun RentaDto.toEntity() = RentaEntity(
    rentaId = rentaId,
    clienteId = clienteId,
    vehiculoId = vehiculoId,
    fechaRenta = fechaRenta,
    fechaEntrega = fechaEntrega,
    total =  total
)