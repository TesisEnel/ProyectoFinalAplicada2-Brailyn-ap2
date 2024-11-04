package ucne.edu.proyectofinalaplicada2.data.remote

import ucne.edu.proyectofinalaplicada2.data.remote.api.RentCarApi
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import javax.inject.Inject

class RentCarRemoteDataSource @Inject constructor(
    private val rentCarApi: RentCarApi
) {
    suspend fun getClientes() = rentCarApi.getClientes()
    suspend fun addCliente(clienteDto: ClienteDto) = rentCarApi.addCliente(clienteDto)
    suspend fun updateCliente(clienteDto: ClienteDto) = rentCarApi.updateCliente(clienteDto)

}