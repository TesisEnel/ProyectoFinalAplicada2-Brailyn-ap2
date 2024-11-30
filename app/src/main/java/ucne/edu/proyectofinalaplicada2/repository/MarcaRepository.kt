package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.MarcaDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class MarcaRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val marcaDao: MarcaDao
) {
    fun getMarcas(): Flow<Resource<List<MarcaEntity>>> = flow{
        emit(Resource.Loading())
        val localVehiculos = marcaDao.getAll().firstOrNull()
        if(!localVehiculos.isNullOrEmpty()){
            emit(Resource.Success(localVehiculos))
        }
        try {
            emit(Resource.Loading())
            val marcas = rentCarRemoteDataSource.getMarcas()
            marcas.forEach{marca -> marcaDao.save(marca.toEntity())}
            val updatedLocalMarcas = marcaDao.getAll().firstOrNull()
            emit(Resource.Success(updatedLocalMarcas?: emptyList()))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    suspend fun getMarcaById(id: Int): Resource<MarcaEntity?>{
        return try {
            val marca = marcaDao.getByMarcaIdId(id)
            Resource.Success(marca)
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        }
        catch (e: Exception) {
            Resource.Error("Error desconocido ${e.message}")
        }
    }
}