package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity

@Dao
interface ModeloDao {
    @Upsert
    suspend fun save(modelo: ModeloEntity)

    @Query(
        """select * from Modelos where modeloId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): ModeloEntity?
    @Delete
    suspend fun delete(modelo: ModeloEntity)
    @Query("SELECT * FROM Modelos")
    fun getAll(): Flow<List<ModeloEntity>>
    @Query(
        """
    SELECT * 
    FROM Modelos 
    WHERE marcaId = :marcaId
    """
    )
    suspend fun findByMarcaId(marcaId: Int): List<ModeloEntity>
}