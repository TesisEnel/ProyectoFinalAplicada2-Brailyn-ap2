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

    suspend fun getModelos(): Resource<List<ModeloEntity>>  {
        return try {
            val modelos = rentCarRemoteDataSource.getModelos()
            modelos.forEach { modelo -> modeloDao.save(modelo.toEntity())}
            val localModelos = modeloDao.getAll().firstOrNull()
            Resource.Success(localModelos?: emptyList())
        } catch (e: HttpException) {
           Resource.Error("Error desconocido ${e.message}")
        } catch (e: Exception) {
            val localModelos = modeloDao.getAll().firstOrNull()
            Resource.Success(localModelos?: emptyList())
        }
    }

    suspend fun getListModelosByMarcaId(marcaId: Int): Resource<List<ModeloEntity>> {
        return try{
            val modelos = rentCarRemoteDataSource.getModelos()
            modelos.forEach{ modelo -> modeloDao.save(modelo.toEntity())}
            val localModelos = modeloDao.findByMarcaId(marcaId)
            Resource.Success(localModelos)
        }catch (e: HttpException){
            Resource.Error("Error de internet ${e.message}")
        }
        catch (e: Exception){
            val localModelos = modeloDao.findByMarcaId(marcaId)
            Resource.Success(localModelos)
        }
    }

    suspend fun getModeloByMarcaId(marcaId: Int): Resource<ModeloEntity> {
        return try {
            val modelos = modeloDao.getModeloByMarcaId(marcaId)
            Resource.Success(modelos)
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error desconocido ${e.message}")

        }
    }

}