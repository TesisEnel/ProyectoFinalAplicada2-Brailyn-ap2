package ucne.edu.proyectofinalaplicada2.data.remote.dto

data class MarcaDto (
    val marcaId: Int,
    val nombreMarca: String,
    val modelos: List<ModeloDto>
)