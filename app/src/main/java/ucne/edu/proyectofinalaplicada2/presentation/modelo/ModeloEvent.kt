package ucne.edu.proyectofinalaplicada2.presentation.modelo

interface ModeloEvent {
    data class GetModeloConVehiculos(val marcaId: Int) : ModeloEvent
}