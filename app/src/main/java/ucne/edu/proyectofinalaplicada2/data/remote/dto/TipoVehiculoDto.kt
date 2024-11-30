package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity

data class TipoVehiculoDto(
    val tipoVehiculoId: Int,
    val nombreTipoVehiculo: String

)
fun TipoVehiculoDto.toEntity() = TipoVehiculoEntity(
    tipoVehiculoId = tipoVehiculoId,
    nombreTipoVehiculo = nombreTipoVehiculo
)
