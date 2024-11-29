package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class RentaDto(
    val rentaId: Int? = null,
    val clienteId: Int?,
    val vehiculoId: Int?,
    val fechaRenta: String?,
    val fechaEntrega: String?,
    val total: Double?
)
