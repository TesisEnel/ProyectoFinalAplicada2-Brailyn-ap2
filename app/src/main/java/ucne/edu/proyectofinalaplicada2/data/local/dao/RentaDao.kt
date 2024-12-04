package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity

@Dao
interface RentaDao {
    @Upsert
    suspend fun save(renta: RentaEntity)

    @Update
    suspend fun update(renta: RentaEntity)

    @Query(
        """select * from Rentas where rentaId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): RentaEntity?
    @Delete
    suspend fun delete(renta: RentaEntity)

    @Query("SELECT * FROM Rentas")
    fun getAll(): Flow<List<RentaEntity>>

}