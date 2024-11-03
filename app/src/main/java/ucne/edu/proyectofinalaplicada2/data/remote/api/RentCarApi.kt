package ucne.edu.proyectofinalaplicada2.data.remote.api

import retrofit2.http.GET
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto

interface RentCarApi {
    @GET("/api/Clientes")
    suspend fun getClientes(): List<ClienteDto>
}