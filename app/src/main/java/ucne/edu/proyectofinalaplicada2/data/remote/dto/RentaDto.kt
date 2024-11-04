package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class RentaDto(
    val rentaId: Int?,
    val clienteId: Int?,
    val vehiculoId: Int?,
    val fechaRenta: String?,
    val fechaEntrega: String?,
    val total: Int?
)
