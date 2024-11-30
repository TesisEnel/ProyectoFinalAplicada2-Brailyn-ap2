package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.RentaDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class RentaRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val rentaDao: RentaDao
) {
     fun getRentas(): Flow<Resource<List<RentaEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val rentas = rentCarRemoteDataSource.getRentas()
            rentas.forEach{ vehiculo -> rentaDao.save(vehiculo.toEntity())}
            val updatedLocalRentas = rentaDao.getAll().firstOrNull()
            emit(Resource.Success(updatedLocalRentas?: emptyList()))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            val localRentas = rentaDao.getAll().firstOrNull()
            if(!localRentas.isNullOrEmpty()){
                emit(Resource.Success(localRentas))
            }
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


}