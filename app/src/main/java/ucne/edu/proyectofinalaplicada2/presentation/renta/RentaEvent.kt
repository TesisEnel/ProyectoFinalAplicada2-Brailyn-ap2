package ucne.edu.proyectofinalaplicada2.presentation.renta

import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto

sealed interface RentaEvent {
    data class Save(val renta: RentaDto) : RentaEvent
    data class OnchangeClienteId(val clienteId: Int) : RentaEvent
    data class OnchangeVehiculoId(val vehiculoId: Int) : RentaEvent
    data class OnchangeFechaRenta(val fechaRenta: String) : RentaEvent
    data class OnchangeFechaEntrega(val fechaEntrega: String) : RentaEvent
    data class OnchangeTotal(val total: Int) : RentaEvent
    data class CalculeTotal(val fechaRenta: String, val fechaEntrega: String, val costoDiario: Int) : RentaEvent
    data class PrepareRentaData(val emailCliente: String?, val vehiculoId: Int) : RentaEvent
    data object ConfirmRenta : RentaEvent
}