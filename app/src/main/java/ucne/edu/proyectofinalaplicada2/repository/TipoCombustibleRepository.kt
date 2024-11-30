package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.TipoCombustibleDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class TipoCombustibleRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val tipoCombustibleDao: TipoCombustibleDao
){
    suspend fun getTiposCombustibles(): Resource<List<TipoCombustibleEntity>> {
        return try {
            val tipoCombustibles = rentCarRemoteDataSource.getTiposCombustibles()
            tipoCombustibles.forEach{ tipoCombustible -> tipoCombustibleDao.save(tipoCombustible.toEntity()) }
            val tipoCombustiblesLocal = tipoCombustibleDao.getAll().firstOrNull()
            Resource.Success(tipoCombustiblesLocal?: emptyList())
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        } catch (e: Exception) {
            val tipoCombustiblesLocal = tipoCombustibleDao.getAll().firstOrNull()
            Resource.Success(tipoCombustiblesLocal?: emptyList())
        }
    }

}