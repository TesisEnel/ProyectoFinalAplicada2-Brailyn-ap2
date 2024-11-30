package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.ModeloDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class ModeloRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val modeloDao: ModeloDao
) {
    suspend fun getModelosById(id: Int): Resource<ModeloEntity?>  {
        return try {
            val modelos = rentCarRemoteDataSource.getModelos()
            modelos.forEach{ modelo -> modeloDao.save(modelo.toEntity())}
            val modelo = modeloDao.find(id)
            Resource.Success(modelo)
        }catch (e: Exception) {
            val modelo = modeloDao.find(id)
            Resource.Success(modelo)
        }
        catch (e: HttpException){
            Resource.Error("Error de internet ${e.message}")
        }
    }

    fun getModelos(): Flow<Resource<List<ModeloEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val modelos = rentCarRemoteDataSource.getModelos()
            modelos.forEach { modelo -> modeloDao.save(modelo.toEntity())}
            val localModelos = modeloDao.getAll().firstOrNull()
            emit(Resource.Success(localModelos?: emptyList()))
        } catch (e: HttpException) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        } catch (e: Exception) {
            val localModelos = modeloDao.getAll().firstOrNull()
            emit(Resource.Success(localModelos?: emptyList()))
        }
    }

}