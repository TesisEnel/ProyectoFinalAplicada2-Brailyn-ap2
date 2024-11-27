package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

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
    val proveedorId: Int?

)
