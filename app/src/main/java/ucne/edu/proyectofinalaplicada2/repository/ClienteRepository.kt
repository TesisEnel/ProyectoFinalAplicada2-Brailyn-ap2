package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow {
        try {
            emit(Resource.Loading())
            val clientes = rentCarRemoteDataSource.getClientes()
            emit(Resource.Success(clientes))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
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
            val cliente = rentCarRemoteDataSource.updateCliente(id, clienteDto)
            emit(Resource.Success(cliente))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun getClienteByEmail(email: String): Flow<Resource<ClienteDto?>> = flow {
        emit(Resource.Loading()) // Emitir estado de carga primero
        try {
            val cliente = rentCarRemoteDataSource.getClienteByEmail(email)
            emit(Resource.Success(cliente)) // Emitir resultado exitoso
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }



    fun clienteNotExist(email: String, clientes: List<ClienteDto>): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val existeCliente = clientes.any { it.email == email }
            if (existeCliente) {
                emit(Resource.Error("cliente existe"))
            }
            emit(Resource.Success(existeCliente))
        } catch (e: HttpException) {
            emit(Resource.Error("false"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }

    }
}