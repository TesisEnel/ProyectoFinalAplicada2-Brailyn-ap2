package ucne.edu.proyectofinalaplicada2.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import ucne.edu.proyectofinalaplicada2.data.remote.api.RentCarApi
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.MarcaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.TipoCombustibleDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import javax.inject.Inject

class RentCarRemoteDataSource @Inject constructor(
    private val rentCarApi: RentCarApi
) {
    //cliente
    suspend fun getClientes() = rentCarApi.getClientes()
    suspend fun addCliente(clienteDto: ClienteDto) = rentCarApi.addCliente(clienteDto)
    suspend fun getClienteByEmail(email: String) = rentCarApi.getClienteByEmail(email)

    //renta
    suspend fun getRentas() = rentCarApi.getRentas()
    suspend fun addRenta(rentaDto: RentaDto) = rentCarApi.addRenta(rentaDto)
    suspend fun updateRenta(id: Int, rentaDto: RentaDto) = rentCarApi.updateRenta(id, rentaDto)

    //vehiculo
    suspend fun getVehiculos() = rentCarApi.getVehiculos()
    suspend fun getVehiculoById(id: Int) = rentCarApi.getVehiculoById(id)
    suspend fun addVehiculo(

        tipoCombustibleId: RequestBody,
        tipoVehiculoId: RequestBody,
        proveedorId: RequestBody,
        precio: RequestBody,
        descripcion: RequestBody,
        marcaId: RequestBody,
        modeloId: RequestBody,
        images: List<MultipartBody.Part>,
        anio: RequestBody,

    ) = rentCarApi.addVehiculo(

        tipoCombustibleId,
        tipoVehiculoId,
        proveedorId,
        precio,
        descripcion,
        marcaId,
        modeloId,
        images,
        anio
    )

    suspend fun updateVehiculo(id: Int, vehiculoDto: VehiculoDto) =
        rentCarApi.updateVehiculo(id, vehiculoDto)

    //Marca
    suspend fun getMarcas() = rentCarApi.getMarcas()
    suspend fun getMarcaById(id: Int) = rentCarApi.getMarcaById(id)


    //TipoCombustible
    suspend fun getTiposCombustibles() = rentCarApi.getTipoCombustibles()


    //TipoVehiculo
    suspend fun getTiposVehiculos() = rentCarApi.getTipoVehiculos()

    //Proveedor
    suspend fun getProveedores() = rentCarApi.getProveedores()

    //Modelo
    suspend fun getModelosById(id: Int) = rentCarApi.getModelosById(id)
    suspend fun getModelos() = rentCarApi.getModelos()

}