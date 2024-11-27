package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.local.dao.VehiculoDao
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.utils.Constant
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.io.File
import javax.inject.Inject

class VehiculoRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource,
    private val vehiculoDao: VehiculoDao
){
    fun getVehiculos(): Flow<Resource<List<VehiculoEntity>>> = flow {

        emit(Resource.Loading())
        val localVehiculos = vehiculoDao.getAll().firstOrNull()
        if(!localVehiculos.isNullOrEmpty()){
            emit(Resource.Success(localVehiculos))
        }
        try {
            emit(Resource.Loading())
            val vehiculos = rentCarRemoteDataSource.getVehiculos()
            vehiculos.forEach{vehiculo -> vehiculoDao.save(vehiculo.toEntity())}
            val updatedLocalVehiculos = vehiculoDao.getAll().firstOrNull()
            emit(Resource.Success(updatedLocalVehiculos?: emptyList()))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun getVehiculoById(id: Int): Flow<Resource<VehiculoDto>> = flow {
        try {
            emit(Resource.Loading())
            val vehiculo = rentCarRemoteDataSource.getVehiculos().first{ it.vehiculoId == id}
            emit(Resource.Success(vehiculo))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        }
        catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

    fun addVehiculo(
      vehiculoDto:VehiculoDto
    ): Flow<Resource<VehiculoDto>> =
        flow {
            try {
                emit(Resource.Loading())
                val requestoBodyTipoCombustibleId =
                    vehiculoDto.tipoCombustibleId.toString().toRequestBody(Constant.TEXT_PLAIN .toMediaTypeOrNull())
                val requestoBodyTipoVehiculoId =
                    vehiculoDto.tipoVehiculoId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyProveedorId =
                    vehiculoDto.proveedorId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyPrecio =
                    vehiculoDto.precio.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyDescripcion =
                    vehiculoDto.descripcion?.toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodymarcaId =
                    vehiculoDto.marcaId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodymodeloId =
                    vehiculoDto.modeloId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyanio =
                    vehiculoDto.anio.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())

                val updatedImages=vehiculoDto.imagePath.map {imagen ->
                    val img = File(imagen?:"")
                    val requestBodyFile = img.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("images", img.name, requestBodyFile)
                }


                val vehiculo = rentCarRemoteDataSource.addVehiculo(
                    requestoBodyTipoCombustibleId,
                    requestoBodyTipoVehiculoId,
                    requestoBodyProveedorId,
                    requestoBodyPrecio,
                    requestoBodyDescripcion,
                    requestoBodymarcaId,
                    requestoBodymodeloId,
                    updatedImages,
                    requestoBodyanio
                )
                emit(Resource.Success(vehiculo))

            } catch (e: HttpException) {
                emit(Resource.Error("Error de internet ${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error("Error desconocido ${e.message}"))
            }
        }

}