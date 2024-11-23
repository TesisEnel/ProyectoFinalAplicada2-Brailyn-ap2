package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.*
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VehiculoViewModel @Inject constructor(
    private val vehiculoRepository: VehiculoRepository,
    private val marcaRepository: MarcaRepository,
    private val tipoCombustibleRepository: TipoCombustibleRepository,
    private val tipoVehiculoRepository: TipoVehiculoRepository,
    private val proveedorRepository: ProveedorRepository,
    private val modeloRepository: ModeloRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(Uistate())
    val uistate = _uistate.asStateFlow()

    init {
        getVehiculos()
        getMarcas()
        getTiposCombustibles()
        getTiposVehiculos()
        getProveedores()
    }

    private fun getVehiculos() {
        viewModelScope.launch {
            vehiculoRepository.getVehiculos().collect { result ->

                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                vehiculos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getMarcas() {
        viewModelScope.launch {
            marcaRepository.getMarcas().collect { result ->

                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                marcas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getTiposCombustibles() {
        viewModelScope.launch {
            tipoCombustibleRepository.getTiposCombustibles().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                tiposCombustibles = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getTiposVehiculos() {
        viewModelScope.launch {
            tipoVehiculoRepository.getTiposVehiculos().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(

                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                tiposVehiculos = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }

        }

    }

    private fun getProveedores() {
        viewModelScope.launch {
            proveedorRepository.getProveedores().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(

                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                proveedores = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }
        }

    }

    private fun getModelos(id: Int) {
        viewModelScope.launch {
            modeloRepository.getModelos(id).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                modelos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }

    }

    private fun save() {
        viewModelScope.launch {
            val precio = uistate.value.precio
            val descripcion = uistate.value.descripcion
            val imagen = uistate.value.imagePath
            val tipoCombustibleId =uistate.value.tipoCombustibleId ?: 0
            val tipoVehiculoId =uistate.value.tipoVehiculoId ?: 0
            val proveedorId =uistate.value.proveedorId ?: 0
            val marcaId =uistate.value.marcaId
            val modeloId =uistate.value.modeloId
            val anio = uistate.value.anio

            val vehiculo = vehiculoRepository.addVehiculo(
                tipoCombustibleId = tipoCombustibleId ,
                tipoVehiculoId = tipoVehiculoId,
                proveedorId = proveedorId,
                precio = precio ?: 0,
                descripcion = descripcion,
                marcaId = marcaId,
                modeloId = modeloId,
                images = imagen,
                anio = anio ?: 0
            )

            vehiculo.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoadingData = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Vehiculo agregado",
                                isLoadingData = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onChangePrecio(precio: Int) {
        _uistate.update {
            it.copy(
                precio = precio
            )
        }
    }

    private fun onChangeDescripcion(descripcion: String) {
        _uistate.update {
            it.copy(
                descripcion = descripcion
            )
        }
    }

    private fun onChangeTipoCombustibleId(tipoCombustibleId: Int) {
        _uistate.update {
            it.copy(
                tipoCombustibleId = tipoCombustibleId
            )
        }

    }

    private fun onChangeTipoVehiculoId(tipoVehiculoId: Int) {
        _uistate.update {
            it.copy(
                tipoVehiculoId = tipoVehiculoId
            )
        }
    }

    private fun onChangeMarcaId(marcaId: Int) {
        getModelos(marcaId)
        getVehiculos()
        getMarcas()
        _uistate.update {
            it.copy(
                marcaId = marcaId,
            )
        }

    }
    private fun onChangeModeloId(modeloId: Int) {
        _uistate.update {
            it.copy(
                modeloId = modeloId
            )
        }
    }
    private fun onChangeProveedorId(proveedorId: Int) {
        _uistate.update {
            it.copy(
                proveedorId = proveedorId
            )
        }
    }

    private fun onChangeImagePath(imagesPath: List<File>) {
        val updatedImage = imagesPath.map { image->
            image.absolutePath
        }
        _uistate.update {
            it.copy(
                imagePath = updatedImage
            )
        }
    }
    private fun onChangeAnio(anio: Int) {
        _uistate.update {
            it.copy(
                anio = anio
            )
        }
    }

    fun nuevo() {
        _uistate.update {
            it.copy(
                vehiculoId = null,
                tipoCombustibleId = null,
                tipoVehiculoId = null,
                success = "",
                error = "",
            )
        }
    }

    fun onEvent(event: VehiculoEvent) {
        when (event) {
            is VehiculoEvent.OnChangeDescripcion -> onChangeDescripcion(event.descripcion)
            is VehiculoEvent.OnChangePrecio -> onChangePrecio(event.precio)
            is VehiculoEvent.OnChangeTipoCombustibleId -> onChangeTipoCombustibleId(event.tipoCombustibleId)
            is VehiculoEvent.OnChangeTipoVehiculoId -> onChangeTipoVehiculoId(event.tipoVehiculoId)
            is VehiculoEvent.OnChangeMarcaId -> onChangeMarcaId(event.marcaId)
            is VehiculoEvent.OnChangeModeloId -> onChangeModeloId(event.modeloId)
            is VehiculoEvent.OnChangeImagePath -> onChangeImagePath(event.imagePath)
            is VehiculoEvent.OnChangeProveedorId -> onChangeProveedorId(event.proveedorId)
            VehiculoEvent.Save -> save()
            is VehiculoEvent.GetModelos -> getModelos(event.marcaId)
            VehiculoEvent.GetVehiculos -> getVehiculos()
            is VehiculoEvent.OnChangeAnio -> onChangeAnio(event.anio)
        }
    }
}