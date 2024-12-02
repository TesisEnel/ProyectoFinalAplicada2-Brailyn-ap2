package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.TipoVehiculoDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class TipoVehiculoRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val tipoVehiculoDao: TipoVehiculoDao

) {
    suspend fun getTiposVehiculos(): Resource<List<TipoVehiculoEntity>> {
        return try {
            val tiposVehiculos = rentCarRemoteDataSource.getTiposVehiculos()
            tiposVehiculos.forEach { tipoVehiculoDao.save(it.toEntity()) }
            val tipoVehiculosLocal = tipoVehiculoDao.getAll().firstOrNull()
            Resource.Success(tipoVehiculosLocal?: emptyList())
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        } catch (e: Exception) {
            val tipoVehiculosLocal = tipoVehiculoDao.getAll().firstOrNull()
            Resource.Success(tipoVehiculosLocal?: emptyList())
        }
    }

    suspend fun getTipoVehiculoById(id: Int): Resource<TipoVehiculoEntity?> {
        return try {
            val tiposVehiculos = rentCarRemoteDataSource.getTiposVehiculos()
            tiposVehiculos.forEach { tipoVehiculoDao.save(it.toEntity()) }
            val tipoVehiculo = tipoVehiculoDao.find(id)
            Resource.Success(tipoVehiculo)
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        }
        catch (e: Exception) {
            val tipoVehiculo = tipoVehiculoDao.find(id)
            Resource.Success(tipoVehiculo)
        }
    }


}