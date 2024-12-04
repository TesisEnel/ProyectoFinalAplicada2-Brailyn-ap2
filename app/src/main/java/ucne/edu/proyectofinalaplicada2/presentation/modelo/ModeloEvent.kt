package ucne.edu.proyectofinalaplicada2.presentation.modelo

interface ModeloEvent {
    data class GetModeloConVehiculos(val marcaId: Int,val isAdmin:Boolean) : ModeloEvent
    data class GetVehiculosByMarcaId(val marcaId: Int, val isAdmin:Boolean) : ModeloEvent
    data class SetFilter(val filter: ModeloFilter) : ModeloEvent

}