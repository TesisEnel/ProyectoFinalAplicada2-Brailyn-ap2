package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

sealed interface VehiculoEvent {
    data object Save : VehiculoEvent
    data class OnchangeMarca(val marca: String) : VehiculoEvent
    data class OnchangeModelo(val modelo: String) : VehiculoEvent
    data class OnchangePrecio(val precio: Int) : VehiculoEvent
    data class OnchangeDescripcion(val descripcion: String) : VehiculoEvent
    data class OnchangeTipoCombustibleId(val tipoCombustibleId: Int) : VehiculoEvent
    data class OnchangeTipoVehiculoId(val tipoVehiculoId: Int) : VehiculoEvent
}