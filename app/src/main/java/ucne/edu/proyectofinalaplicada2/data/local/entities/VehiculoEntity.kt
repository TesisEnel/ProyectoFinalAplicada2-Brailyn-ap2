package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

@Entity(tableName = "Vehiculos")
data class VehiculoEntity(
    @PrimaryKey
    val vehiculoId: Int?,
    val tipoCombustibleId: Int?,
    val tipoVehiculoId: Int?,
    val marcaId: Int?,
    val modeloId: Int?,
    val precio: Int?,
    val descripcion: String?,
    val anio: Int?,
    val imagePath: List<String?>?  ,
    val proveedorId: Int?,
    val estaRentado: Boolean?,
)

fun VehiculoEntity.toVehiculoDto() = VehiculoDto(
    vehiculoId = vehiculoId,
    tipoCombustibleId = tipoCombustibleId,
    tipoVehiculoId = tipoVehiculoId,
    marcaId = marcaId,
    modeloId = modeloId,
    precio = precio,
    descripcion = descripcion,
    anio = anio,
    imagePath = imagePath,
    proveedorId = proveedorId,
    estaRentado = estaRentado,
)
