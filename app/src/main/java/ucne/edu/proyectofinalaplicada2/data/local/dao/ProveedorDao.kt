package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity

@Dao
interface ProveedorDao {
    @Upsert
    suspend fun save(proveedor: ProveedorEntity)

    @Query(
        """select * from Proveedores where proveedorId=:id
            Limit 1
        """
        )
    suspend fun find(id: Int): ProveedorEntity?

    @Delete
    suspend fun delete(proveedor: ProveedorEntity)

    @Query("SELECT * FROM Proveedores")
    suspend fun getAll(): List<ProveedorEntity>

}