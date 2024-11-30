package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity

data class ModeloDto(
    val modeloId: Int,
    var marcaId: Int,
    val modeloVehiculo: String,
    )

fun ModeloDto.toEntity() = ModeloEntity(
    modeloId = modeloId,
    marcaId = marcaId,
    modeloVehiculo = modeloVehiculo,
)