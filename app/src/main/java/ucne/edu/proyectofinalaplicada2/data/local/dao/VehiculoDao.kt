package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity

@Dao
interface VehiculoDao {
    @Upsert
    suspend fun save(vehiculo: VehiculoEntity)

    @Query(
        """select * from Vehiculos where vehiculoId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): VehiculoEntity?
    @Delete
    suspend fun delete(vehiculo: VehiculoEntity)

    @Query("SELECT * FROM Vehiculos")
    suspend fun getAll(): List<VehiculoEntity>


}