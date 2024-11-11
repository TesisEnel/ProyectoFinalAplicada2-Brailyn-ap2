package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

data class Uistate(
    val vehiculoId: Int? = null,
    val tipoCombustibleId: Int? = null,
    val tipoVehiculoId: Int? = null,
    val marcaId: Int = 0,
    val modeloId: Int = 0,
    val precio: Int = 0,
    val descripcion: String = "",
    val imagePath: String = "",
    val vehiculos: List<VehiculoDto> = emptyList(),
    val marcas: List<MarcaDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = ""
)

fun Uistate.toEntity() = VehiculoDto(
    vehiculoId = vehiculoId,
    tipoCombustibleId = tipoCombustibleId,
    tipoVehiculoId = tipoVehiculoId,
    marcaId = marcaId,
    modeloId = modeloId,
    precio = precio,
    descripcion = descripcion,
    imagePath = imagePath
)
