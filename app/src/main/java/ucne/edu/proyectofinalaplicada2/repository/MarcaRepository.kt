package ucne.edu.proyectofinalaplicada2.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.proyectofinalaplicada2.data.remote.RentCarRemoteDataSource
import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.toEntity
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.MarcaVehiculoUiState
import ucne.edu.proyectofinalaplicada2.utils.Constant
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

class MarcaRepository @Inject constructor(
    private val rentCarRemoteDataSource: RentCarRemoteDataSource
) {
    fun getMarcas(): Flow<Resource<List<MarcaDto>>> = flow{
        try {
            emit(Resource.Loading())
            val marcas = rentCarRemoteDataSource.getMarcas()
            emit(Resource.Success(marcas))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
    fun getVehiculoConMarca(): Flow<Resource<List<MarcaVehiculoUiState>>> = flow{
        try {
            emit(Resource.Loading())
            val marcas = rentCarRemoteDataSource.getMarcas()
            val vehiculos = rentCarRemoteDataSource.getVehiculos().map { it.toEntity() }
            val marcaConVehiculos = marcas.map { marca ->
                val vehiculosMarca = vehiculos.filter { vehiculo ->
                    vehiculo.marcaId == marca.marcaId
                }

                if (vehiculosMarca.isNotEmpty()) {
                    MarcaVehiculoUiState(
                        marcaId = marca.marcaId,
                        nombreMarca = marca.nombreMarca,
                        vehiculos = vehiculosMarca,
                        imageUrl = Constant.URL_BLOBSTORAGE + vehiculosMarca.firstOrNull()?.imagePath?.firstOrNull()
                    )
                } else {
                    throw Exception("No hay vehiculos")
                }

            }
            emit(Resource.Success(marcaConVehiculos))

        } catch (httpException: HttpException) {
            emit(Resource.Error("Error de internet ${httpException.message}"))
        }catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }
    fun getMarcaById(id: Int): Flow<Resource<MarcaDto>> = flow{
        try {
            emit(Resource.Loading())
            val marca = rentCarRemoteDataSource.getMarcaById(id)
            emit(Resource.Success(marca))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet ${e.message}"))
        }
        catch (e: Exception) {
            emit(Resource.Error("Error desconocido ${e.message}"))
        }
    }


}