package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.TipoVehiculoDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class TipoVehiculoRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
    fun getTiposVehiculos(): Flow<Resource<List<TipoVehiculoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val tiposVehiculos = rentCarRemoteDataSource.getTiposVehiculos()
            emit(Resource.Success(tiposVehiculos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
}