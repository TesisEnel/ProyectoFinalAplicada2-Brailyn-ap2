package ucne.edu.proyectofinalaplicada2.data.remote.dto

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity

data class VehiculoDto(
    val vehiculoId: Int?,
    val tipoCombustibleId: Int?,
    val tipoVehiculoId: Int?,
    val marcaId: Int?,
    val modeloId: Int?,
    val precio: Int?,
    val descripcion: String?,
    val anio: Int?,
    val imagePath: List<String?>,
    val proveedorId: Int?,
    val marca: MarcaEntity?
)

fun VehiculoDto.toEntity() = VehiculoEntity(
    vehiculoId,
    tipoCombustibleId,
    tipoVehiculoId,
    marcaId,
    modeloId,
    precio,
    descripcion,
    anio,
    imagePath,
    proveedorId
)
