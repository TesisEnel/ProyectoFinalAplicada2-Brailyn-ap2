package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.ProveedorDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class ProveedorRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val proveedorDao: ProveedorDao
) {
    suspend fun getProveedores(): Resource<List<ProveedorEntity>> {
        return try {
            val proveedores = rentCarRemoteDataSource.getProveedores()
            proveedores.forEach {  proveedor ->  proveedorDao.save(proveedor.toEntity()) }
            val proveedoresLocal = proveedorDao.getAll().firstOrNull()
            Resource.Success(proveedoresLocal?: emptyList())
        } catch (e: HttpException) {
            Resource.Error("Error de internet ${e.message}")
        } catch (e: Exception) {
           val proveedoresLocal = proveedorDao.getAll().firstOrNull()
            Resource.Success(proveedoresLocal?: emptyList())
        }
    }
}