package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.io.File
import javax.inject.Inject

class VehiculoRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
){
    fun getVehiculos(): Flow<Resource<List<VehiculoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val vehiculos = rentCarRemoteDataSource.getVehiculos()
            emit(Resource.Success(vehiculos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }


    fun addVehiculo(
        tipoCombustibleId: Int,
        tipoVehiculoId: Int,
        proveedorId: Int,
        precio: Int,
        descripcion: String,
        images: List<String>,
        marcaId: Int,
        modeloId: Int
    ): Flow<Resource<VehiculoDto>> =
        flow {
            try {
                emit(Resource.Loading())
                val requestoBodyTipoCombustibleId =
                    tipoCombustibleId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodyTipoVehiculoId =
                    tipoVehiculoId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodyProveedorId =
                    proveedorId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodyPrecio =
                    precio.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodyDescripcion =
                    descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodymarcaId =
                    marcaId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestoBodymodeloId =
                    modeloId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                val updatedImages=images.map {imagen ->
                    val img = File(imagen)
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
                )
                emit(Resource.Success(vehiculo))

            } catch (e: HttpException) {
                emit(Resource.Error("Error de internet ${e.message}"))
            } catch (e: Exception) {
                emit(Resource.Error("Error desconocido ${e.message}"))
            }
        }

    fun updateVehiculo(id: Int, vehiculoDto: VehiculoDto): Flow<Resource<VehiculoDto>> = flow {
        try {
            emit(Resource.Loading())
            val vehiculo = rentCarRemoteDataSource.updateVehiculo(id, vehiculoDto)
            emit(Resource.Success(vehiculo))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }

}