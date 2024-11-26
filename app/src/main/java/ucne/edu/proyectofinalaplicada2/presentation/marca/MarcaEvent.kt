package ucne.edu.proyectofinalaplicada2.presentation.marca

sealed interface MarcaEvent {
    data class OnchangeMarcaId(val marcaId: Int) : MarcaEvent
    data class OnchangeNombreMarca(val nombreMarca: String) : MarcaEvent
}