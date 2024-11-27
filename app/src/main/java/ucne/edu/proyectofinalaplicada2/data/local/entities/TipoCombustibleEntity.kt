package ucne.edu.proyectofinalaplicada2.data.local.entities;

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiposCombustibles")
class TipoCombustibleEntity (
    @PrimaryKey
    val tipoCombustibleId: Int,
    val nombreTipoCombustible: String
)
