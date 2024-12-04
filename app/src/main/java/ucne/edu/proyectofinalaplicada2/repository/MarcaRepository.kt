package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.firstOrNull
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
    suspend fun getMarcas(): Resource<List<MarcaEntity>> {
        val localVehiculos = marcaDao.getAll().firstOrNull()
        if(!localVehiculos.isNullOrEmpty()){
            return Resource.Success(localVehiculos)
        }
        return try {
            val marcas = rentCarRemoteDataSource.getMarcas()
            marcas.forEach{marca -> marcaDao.save(marca.toEntity())}
            val updatedLocalMarcas = marcaDao.getAll().firstOrNull()
            Resource.Success(updatedLocalMarcas?: emptyList())
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Error desconocido ${e.message}")
        }
    }

    suspend fun getMarcaById(id: Int?): Resource<MarcaEntity?>{
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