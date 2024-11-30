package ucne.edu.proyectofinalaplicada2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Clientes")
class ClienteEntity(
    @PrimaryKey
    val clienteId: Int?,
    val cedula: String?,
    val nombre: String?,
    val apellido: String?,
    val direccion: String?,
    val celular: String?,
    val email: String?,
    val isAdmin: Boolean?,

)