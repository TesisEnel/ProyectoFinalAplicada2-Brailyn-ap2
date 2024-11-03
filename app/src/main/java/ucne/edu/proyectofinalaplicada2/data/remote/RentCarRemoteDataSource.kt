package ucne.edu.proyectofinalaplicada2.data.remote

import ucne.edu.proyectofinalaplicada2.data.remote.api.RentCarApi
import javax.inject.Inject

class RentCarRemoteDataSource @Inject constructor(
    private val rentCarApi: RentCarApi
) {
    suspend fun getClientes() = rentCarApi.getClientes()
}