package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class RentaRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
     fun getRentas(): Flow<Resource<List<RentaDto>>> = flow {
        try {
            emit(Resource.Loading())
            val rentas = rentCarRemoteDataSource.getRentas()
            emit(Resource.Success(rentas))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
    fun addRenta(rentaDto: RentaDto): Flow<Resource<RentaDto>> = flow {
        try {
            emit(Resource.Loading())
            val renta = rentCarRemoteDataSource.addRenta(rentaDto)
            emit(Resource.Success(renta))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
    fun updateRenta(id:Int, rentaDto: RentaDto): Flow<Resource<RentaDto>> = flow {
        try {
            emit(Resource.Loading())
            val renta = rentCarRemoteDataSource.updateRenta(id,rentaDto)
            emit(Resource.Success(renta))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

}