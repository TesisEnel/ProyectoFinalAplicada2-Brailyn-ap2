package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ModeloDto
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.ProveedorRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoVehiculoRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
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
                                vehiculos = result.data ?: emptyList()
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
                                marcas = result.data ?: emptyList()
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
                                tiposCombustibles = result.data ?: emptyList()
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
                                isLoading = true
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
                                isLoading = true
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

    private fun getModelos(id: Int): List<ModeloDto> {
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
                                modelos = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
        return uistate.value.modelos

    }

    private fun save() {
        viewModelScope.launch {
            val precio = uistate.value.precio
            val descripcion = uistate.value.descripcion
            val imagen= uistate.value.imagePath
            val vehiculo = vehiculoRepository.addVehiculo(
                precio = precio?: 0,
                descripcion = descripcion,
                image = imagen
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
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Vehiculo agregado"
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

        _uistate.update {
            it.copy(
                marcaId = marcaId,
                modelos = getModelos(marcaId)
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

    private fun onChangeImagePath(imagePath: File) {
        _uistate.update {
            it.copy(
                imagePath = imagePath.absolutePath
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
            VehiculoEvent.Save -> save()
        }
    }

}