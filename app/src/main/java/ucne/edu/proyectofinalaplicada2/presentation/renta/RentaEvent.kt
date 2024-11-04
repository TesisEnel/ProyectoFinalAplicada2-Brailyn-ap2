package ucne.edu.proyectofinalaplicada2.presentation.renta

sealed interface RentaEvent {
    data object Save : RentaEvent
    data class OnchangeClienteId(val clienteId: Int) : RentaEvent
    data class OnchangeVehiculoId(val vehiculoId: Int) : RentaEvent
    data class OnchangeFechaRenta(val fechaRenta: String) : RentaEvent
    data class OnchangeFechaEntrega(val fechaEntrega: String) : RentaEvent
    data class OnchangeTotal(val total: Int) : RentaEvent

}