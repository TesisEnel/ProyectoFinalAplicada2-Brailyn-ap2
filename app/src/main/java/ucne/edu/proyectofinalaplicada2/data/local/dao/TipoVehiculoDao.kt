package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity

@Dao
interface TipoVehiculoDao {
    @Upsert
    suspend fun save(tipoVehiculo: TipoVehiculoEntity)

    @Query(
        """select * from TiposVehiculos where tipoVehiculoId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): TipoVehiculoEntity?
    @Delete
    suspend fun delete(tipoVehiculo: TipoVehiculoEntity)

    @Query("SELECT * FROM TiposVehiculos")
    fun getAll(): Flow<List<TipoVehiculoEntity>>


}