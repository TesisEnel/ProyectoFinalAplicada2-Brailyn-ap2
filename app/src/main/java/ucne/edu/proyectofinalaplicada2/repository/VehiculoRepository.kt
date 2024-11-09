package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class VehiculoRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
)
{
    fun getVehiculos(): Flow<Resource<List<VehiculoDto>>> = flow{
        try {
            emit(Resource.Loading())
            val vehiculos = rentCarRemoteDataSource.getVehiculos()
            emit(Resource.Success(vehiculos))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
    fun addVehiculo(vehiculoDto: VehiculoDto): Flow<Resource<VehiculoDto>> = flow{
        try {
            emit(Resource.Loading())
            val vehiculo = rentCarRemoteDataSource.addVehiculo(vehiculoDto)
            emit(Resource.Success(vehiculo))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun updateVehiculo(id:Int, vehiculoDto: VehiculoDto): Flow<Resource<VehiculoDto>> = flow{
        try {
            emit(Resource.Loading())
            val vehiculo = rentCarRemoteDataSource.updateVehiculo(id,vehiculoDto)
            emit(Resource.Success(vehiculo))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        }
        catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

}