package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ProveedorEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto

data class VehiculoUistate(
    val vehiculoId: Int? = null,
    val tipoCombustibleId: Int? = null,
    val tipoVehiculoId: Int? = null,
    val proveedorId: Int? = null,
    val marcaId: Int = 0,
    val modeloId: Int = 0,
    val precio: Int? = null,
    val descripcion: String = "",
    val anio: Int? = null,
    val imagePath: List<String?> = emptyList(),
    var isLoading: Boolean? = null,
    var isLoadingData: Boolean? = null,
    val error: String = "",
    val success: String = "",
    val marcas: List<MarcaEntity> = emptyList(),
    val vehiculos: List<VehiculoEntity> = emptyList(),
    val modelos: List<ModeloEntity> = emptyList(),
    val marca: MarcaEntity? = null,
    val vehiculoConMarcas: List<VehiculoConMarca> = emptyList(),
    val tipoCombustibles: List<TipoCombustibleEntity>? = emptyList(),
    val proveedores: List<ProveedorEntity>? = emptyList(),
    val tipoVehiculos: List<TipoVehiculoEntity>? = emptyList(),
    val filteredVehiculoConMarcas: List<VehiculoConMarca> = emptyList(),
    val searchQuery: String = "",
    val filteredListIsEmpty: Boolean = false,
    val estaRentado: Boolean = false,
)
data class VehiculoConMarca(
    val vehiculo: VehiculoEntity,
    val nombreMarca: String?,
    val nombreModelo: String? = null,
    val estaRentado: Boolean? = null
)

fun VehiculoUistate.toEntity()= VehiculoDto(
    vehiculoId = vehiculoId,
    tipoCombustibleId = tipoCombustibleId,
    tipoVehiculoId = tipoVehiculoId,
    marcaId = marcaId,
    modeloId = modeloId,
    precio = precio,
    descripcion = descripcion,
    anio = anio,
    imagePath = imagePath,
    proveedorId = proveedorId,
    marca = marca,
    estaRentado = estaRentado


)