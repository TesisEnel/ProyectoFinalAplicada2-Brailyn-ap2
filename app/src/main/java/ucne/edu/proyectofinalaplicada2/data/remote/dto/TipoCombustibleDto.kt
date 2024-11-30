package ucne.edu.proyectofinalaplicada2.data.remote.dto;

import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity

data class TipoCombustibleDto (
    val tipoCombustibleId: Int,
    val nombreTipoCombustible: String
)

fun TipoCombustibleDto.toEntity()= TipoCombustibleEntity(
    tipoCombustibleId = tipoCombustibleId,
    nombreTipoCombustible = nombreTipoCombustible
)
