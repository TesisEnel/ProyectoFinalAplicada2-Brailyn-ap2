package ucne.edu.proyectofinalaplicada2.repository
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ClienteRepository @Inject constructor(
    private  val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow{
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
}