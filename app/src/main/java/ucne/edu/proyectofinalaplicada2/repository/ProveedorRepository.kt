package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ProveedorDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class ProveedorRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
    fun getProveedores(): Flow<Resource<List<ProveedorDto>>> = flow {
        try {
            emit(Resource.Loading())
            val proveedores = rentCarRemoteDataSource.getProveedores()
            emit(Resource.Success(proveedores))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

}