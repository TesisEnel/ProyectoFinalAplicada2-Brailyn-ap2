package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto

sealed interface RentaEvent {
    data object Save : RentaEvent
    data class OnchangeClienteId(val clienteId: Int) : RentaEvent
    data class OnchangeVehiculoId(val vehiculoId: Int) : RentaEvent
    data class OnchangeFechaRenta(val fechaRenta: String) : RentaEvent
    data class OnchangeFechaEntrega(val fechaEntrega: String) : RentaEvent
    data class OnchangeTotal(val total: Double) : RentaEvent
    data class CalculeTotal(val fechaRenta: String, val fechaEntrega: String, val costoDiario: Int) : RentaEvent
    data class PrepareRentaData(val emailCliente: String?, val vehiculoId: Int) : RentaEvent
    data object ConfirmRenta : RentaEvent
    data object CloseModal : RentaEvent
    data class MostraDatosVehiculoByRole(val isAdmin: Boolean) : RentaEvent
    data object MostraDatosVehiculo : RentaEvent

}