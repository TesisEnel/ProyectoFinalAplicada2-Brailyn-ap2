package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.RentaDao
import ucne.edu.proyectofinalaplicada2.data.local.dao.VehiculoDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.RentaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.toVehiculoDto
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class RentaRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val rentaDao: RentaDao,
    private val vehiculoDao: VehiculoDao
) {
     fun getRentas(): Flow<Resource<List<RentaEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val rentas = rentCarRemoteDataSource.getRentas()
            val localRentas = rentaDao.getAll().firstOrNull()

            val remotIds = rentas.map { it.rentaId }
            val localIds = localRentas?.map { it.rentaId }
            val deleteIds = localIds?.filterNot { remotIds.contains(it) }

            if(deleteIds?.isNotEmpty() == true){
                val rentasToDelete = localRentas.filter { deleteIds.contains(it.rentaId) }
                rentasToDelete.forEach { rentaDao.delete(it) }
            }

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
    fun addRenta(rentaDto: RentaDto, vehiculoEntity: VehiculoEntity?): Flow<Resource<RentaDto>> = flow {
        try {
            emit(Resource.Loading())
            val renta = rentCarRemoteDataSource.addRenta(rentaDto)
            vehiculoDao.updateVehiculo(vehiculoEntity!!)
            rentCarRemoteDataSource.updateVehiculo(vehiculoEntity.vehiculoId?:0, vehiculoEntity.toVehiculoDto())
            emit(Resource.Success(renta))
            } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error, ocurrió algo inesperado"))
        }
    }

    fun updateRenta(rentaDto: RentaDto): Flow<Resource<RentaDto>> = flow {
        try {
            emit(Resource.Loading())
            val renta = rentCarRemoteDataSource.updateRenta(rentaDto.rentaId?:0,rentaDto)
            rentaDao.update(renta.toEntity())
            emit(Resource.Success(renta))
        }
        catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        }
        catch (e: Exception) {
            emit(Resource.Error("Error, favor verificar conexión a internet"))
        }
    }

    fun deleteRenta(id: Int): Flow<Resource<RentaDto>> = flow {
        try {
            emit(Resource.Loading())
            val renta = rentCarRemoteDataSource.deleteRenta(id)
            rentaDao.delete(renta.toEntity())
            emit(Resource.Success(renta))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error, favor verificar conexión a internet ${e.message}"))
        }
    }
}