package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity

@Dao
interface VehiculoDao {
    @Upsert
    suspend fun save(vehiculo: VehiculoEntity)
    @Update
    suspend fun updateVehiculo(vehiculo: VehiculoEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<VehiculoEntity>)

    @Query(
        """select * from Vehiculos where vehiculoId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): VehiculoEntity?
    @Delete
    suspend fun delete(vehiculo: VehiculoEntity)

    @Query(
        """DELETE FROM Vehiculos WHERE vehiculoId IN (:ids)"""
    )
    fun deleteByIds(ids: List<Int>)

    @Query("SELECT * FROM Vehiculos")
    suspend fun getAll(): List<VehiculoEntity>

    @Query(
        """select * from Vehiculos where marcaId=:marcaId
        """
    )
    fun getListVehiculosByMarcaId(marcaId: Int?): Flow<List<VehiculoEntity>>

}