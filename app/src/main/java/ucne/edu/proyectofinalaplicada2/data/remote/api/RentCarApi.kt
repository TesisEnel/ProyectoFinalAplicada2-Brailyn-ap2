package ucne.edu.proyectofinalaplicada2.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto

interface RentCarApi {
    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDto>
    @POST("api/Clientes")
    suspend fun addCliente(@Body clienteDto: ClienteDto): ClienteDto
    @PUT("api/Clientes/{id}")
    suspend fun updateCliente(@Body clienteDto: ClienteDto): ClienteDto
}