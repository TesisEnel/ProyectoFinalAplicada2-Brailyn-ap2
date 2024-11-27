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
import ucne.edu.proyectofinalaplicada2.utils.Constant
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

    fun getVehiculoById(id: Int): Flow<Resource<VehiculoDto>> = flow {
        try {
            emit(Resource.Loading())
            val vehiculo = rentCarRemoteDataSource.getVehiculoById(id)
            emit(Resource.Success(vehiculo))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        }
        catch (e: Exception) {
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
        modeloId: Int,
        anio: Int
    ): Flow<Resource<VehiculoDto>> =
        flow {
            try {
                emit(Resource.Loading())
                val requestoBodyTipoCombustibleId =
                    tipoCombustibleId.toString().toRequestBody(Constant.TEXT_PLAIN .toMediaTypeOrNull())
                val requestoBodyTipoVehiculoId =
                    tipoVehiculoId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyProveedorId =
                    proveedorId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyPrecio =
                    precio.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyDescripcion =
                    descripcion.toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodymarcaId =
                    marcaId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodymodeloId =
                    modeloId.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())
                val requestoBodyanio =
                    anio.toString().toRequestBody(Constant.TEXT_PLAIN.toMediaTypeOrNull())

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