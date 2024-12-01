package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Vehiculos")
class VehiculoEntity(
    @PrimaryKey
    val vehiculoId: Int?,
    val tipoCombustibleId: Int?,
    val tipoVehiculoId: Int?,
    val marcaId: Int?,
    val modeloId: Int?,
    val precio: Int?,
    val descripcion: String?,
    val anio: Int?,
    val imagePath: List<String?>,
    val proveedorId: Int?,
    val estaRentado: Boolean?,

)
