package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Marcas")
class MarcaEntity (
    @PrimaryKey
    val marcaId: Int,
    val nombreMarca: String,
)