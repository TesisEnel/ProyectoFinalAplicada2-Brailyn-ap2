package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiposVehiculos")
class TipoVehiculoEntity(
    @PrimaryKey
    val tipoVehiculoId: Int,
    val nombreTipoVehiculo: String

)
