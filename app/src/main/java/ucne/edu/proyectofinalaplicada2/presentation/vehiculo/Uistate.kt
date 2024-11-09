package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

data class Uistate(
    val vehiculoId: Int? = null,
    val tipoCombustibleId: Int? = null,
    val tipoVehiculoId: Int? = null,
    val marca: String = "",
    val modelo: String = "",
    val precio: Int = 0,
    val descripcion: String = "",
    val vehiculos: List<VehiculoDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val success: String = ""
)

fun Uistate.toEntity() = VehiculoDto(
    vehiculoId = vehiculoId,
    tipoCombustibleId = tipoCombustibleId,
    tipoVehiculoId = tipoVehiculoId,
    marca = marca,
    modelo = modelo,
    precio = precio,
    descripcion = descripcion
)
