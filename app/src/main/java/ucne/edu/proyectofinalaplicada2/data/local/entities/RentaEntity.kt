package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Rentas")
class RentaEntity(
    @PrimaryKey
    val rentaId: Int? = null,
    val clienteId: Int?,
    val vehiculoId: Int?,
    val fechaRenta: String?,
    val fechaEntrega: String?,
    val total: Double?
)
