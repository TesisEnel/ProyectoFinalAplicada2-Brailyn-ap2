package ucne.edu.proyectofinalaplicada2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity

@Dao
interface ClienteDao {

    @Upsert
    suspend fun save(cliente: ClienteEntity)

    @Query(
        """select * from Clientes where clienteId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): ClienteEntity?

    @Delete
    suspend fun delete(cliente: ClienteEntity)

    @Query("SELECT * FROM Clientes")
    suspend fun getAll(): List<ClienteEntity>

    @Query("SELECT * FROM Clientes WHERE email = :email")
    suspend fun getClienteByEmail(email: String): ClienteEntity?



}