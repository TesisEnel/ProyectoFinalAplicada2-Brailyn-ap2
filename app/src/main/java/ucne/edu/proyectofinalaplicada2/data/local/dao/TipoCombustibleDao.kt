package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity

@Dao
interface TipoCombustibleDao {

    @Upsert
    suspend fun save(tipoCombustible: TipoCombustibleEntity)

    @Query(
        """select * from TiposCombustibles where tipoCombustibleId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): TipoCombustibleEntity?
    @Delete
    suspend fun delete(tipoCombustible: TipoCombustibleEntity)

    @Query("SELECT * FROM TiposCombustibles")
    fun getAll(): Flow<List<TipoCombustibleEntity>>

}