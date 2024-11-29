package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity

data class MarcaDto (
    val marcaId: Int,
    val nombreMarca: String,
)
fun MarcaDto.toEntity() = MarcaEntity(
    marcaId = marcaId,
    nombreMarca = nombreMarca,
    )