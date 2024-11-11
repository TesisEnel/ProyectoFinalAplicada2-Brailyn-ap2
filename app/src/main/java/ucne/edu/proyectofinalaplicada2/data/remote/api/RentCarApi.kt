package ucne.edu.proyectofinalaplicada2.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

interface RentCarApi {
    // clientes
    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDto>
    @POST("api/Clientes")
    suspend fun addCliente(@Body clienteDto: ClienteDto): ClienteDto
    @PUT("api/Clientes/{id}")
    suspend fun updateCliente(@Path ("id") id: Int ,@Body clienteDto: ClienteDto): ClienteDto

    // rentas
    @GET("api/Rentas")
    suspend fun getRentas(): List<RentaDto>
    @POST("api/Rentas")
    suspend fun addRenta(@Body rentaDto: RentaDto): RentaDto
    @PUT("api/Rentas/{id}")
    suspend fun updateRenta(@Path ("id") id: Int ,@Body rentaDto: RentaDto): RentaDto

    // vehiculos
    @GET("api/Vehiculos")
    suspend fun getVehiculos(): List<VehiculoDto>
    @POST("api/Vehiculos")
    suspend fun addVehiculo(@Body vehiculoDto: VehiculoDto): VehiculoDto
    @PUT("api/Vehiculos/{id}")
    suspend fun updateVehiculo(@Path ("id") id: Int ,@Body vehiculoDto: VehiculoDto): VehiculoDto



}