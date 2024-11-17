package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class VehiculoDto(
    val vehiculoId: Int?,
    val tipoCombustibleId: Int?,
    val tipoVehiculoId: Int?,
    val marcaId: Int?,
    val modeloId: Int?,
    val precio: Int?,
    val descripcion: String?,
    val imagePath: List<String?>
)