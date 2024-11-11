package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto

sealed interface VehiculoEvent {
    data object Save : VehiculoEvent
    data class OnChangePrecio(val precio: Int) : VehiculoEvent
    data class OnChangeDescripcion(val descripcion: String) : VehiculoEvent
    data class OnChangeTipoCombustibleId(val tipoCombustibleId: Int) : VehiculoEvent
    data class OnChangeTipoVehiculoId(val tipoVehiculoId: Int) : VehiculoEvent
    data class OnChangeMarcaId(val marcaId: Int): VehiculoEvent
}