package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.ClienteDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val clienteDao: ClienteDao
) {
    fun getClientes(): Flow<Resource<List<ClienteEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val clientes = rentCarRemoteDataSource.getClientes()
            clientes.forEach { cliente -> clienteDao.save(cliente.toEntity()) }
            val updatedLocalClientes = clienteDao.getAll()
            emit(Resource.Success(updatedLocalClientes))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            val localClientes = clienteDao.getAll()
            emit(Resource.Success(localClientes))
        }
    }
    suspend fun getClienteByEmail(email: String): Resource<ClienteEntity?>  {
        return try {
            val cliente = clienteDao.getClienteByEmail(email)
            Resource.Success(cliente)
        }
        catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        }
        catch (e: Exception) {
            Resource.Error("Error desconocido ${e.message}")
        }
    }

    fun addCliente(clienteDto: ClienteDto): Flow<Resource<ClienteDto>> = flow {
        try {
            emit(Resource.Loading())
            val cliente = rentCarRemoteDataSource.addCliente(clienteDto)
            emit(Resource.Success(cliente))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun updateCliente(id: Int, clienteDto: ClienteDto): Flow<Resource<ClienteDto>> = flow {
        try {
            emit(Resource.Loading())
            val cliente = rentCarRemoteDataSource.updateCliente(id,clienteDto)
            emit(Resource.Success(cliente))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun clienteNotExist(email: String, clientes: List<ClienteEntity>): Boolean {
        return try {
            clientes.any { it.email == email }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isAdminUser(email: String): Boolean? {
        return getClienteByEmail(email).data?.isAdmin
    }

}