package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Modelos")
class ModeloEntity(
    @PrimaryKey
    val modeloId: Int,
    var marcaId: Int,
    val modeloVehiculo: String,
    )