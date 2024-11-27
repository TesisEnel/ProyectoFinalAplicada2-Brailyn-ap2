package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity

@Dao
interface MarcaDao {
    @Upsert
    suspend fun save(marca: MarcaEntity)

    @Query(
        """select * from Marcas where marcaId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): MarcaEntity?
    @Delete
    suspend fun delete(marca: MarcaEntity)
    @Query("SELECT * FROM Marcas")
    suspend fun getAll(): List<MarcaEntity>

}