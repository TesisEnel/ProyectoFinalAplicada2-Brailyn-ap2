package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class VehiculoDto(
    val vehiculoId: Int?,
    val tipoCombustibleId: Int?,
    val tipoVehiculoId: Int?,
    val marca: String?,
    val modelo: String?,
    val precio: Int?,
    val descripcion: String?,
    val imagePath: String?
)