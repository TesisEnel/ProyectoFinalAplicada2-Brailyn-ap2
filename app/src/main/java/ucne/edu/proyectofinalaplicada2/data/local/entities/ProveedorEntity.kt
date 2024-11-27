package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Proveedores")
class ProveedorEntity(
    @PrimaryKey
    val proveedorId: Int,
    val nombre: String,
    val celular: String,
)
