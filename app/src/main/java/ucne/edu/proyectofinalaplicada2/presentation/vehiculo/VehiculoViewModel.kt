package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VehiculoViewModel @Inject constructor(
    private val vehiculoRepository: VehiculoRepository,
    private val modeloRepository: ModeloRepository,
    private val marcaRepository: MarcaRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(VehiculoUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getVehiculos()
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
    private fun getModelosById(id: Int) {
        viewModelScope.launch {
            modeloRepository.getModelosById(id).collect { result ->
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

            val vehiculo = vehiculoRepository.addVehiculo(
                uistate.value.toEntity()
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

    private fun getMarca(marcaId: Int){
        viewModelScope.launch {
            when(val result = marcaRepository.getNameMarca(marcaId)){
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
                                marca = result.data,
                                isLoading = false
                            )
                        }
                    }
                }

        }
    }

    private suspend fun getMarcaById(id: Int): MarcaEntity? {
        return marcaRepository.getMarcaById(id).data
    }
    private fun onChangePrecio(precio: Int) {
        _uistate.update {
            it.copy(
                precio = precio
            )
        }
    }
    private fun onChangeMarcaId(marcaId: Int) {
        getModelosById(marcaId)
        _uistate.update {
            it.copy(
                marcaId = marcaId
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

    fun onEvent(event: VehiculoEvent) {
        when (event) {
            is VehiculoEvent.OnChangeDescripcion -> onChangeDescripcion(event.descripcion)
            is VehiculoEvent.OnChangePrecio -> onChangePrecio(event.precio)
            is VehiculoEvent.OnChangeTipoCombustibleId -> onChangeTipoCombustibleId(event.tipoCombustibleId)
            is VehiculoEvent.OnChangeTipoVehiculoId -> onChangeTipoVehiculoId(event.tipoVehiculoId)
            is VehiculoEvent.OnChangeModeloId -> onChangeModeloId(event.modeloId)
            is VehiculoEvent.OnChangeImagePath -> onChangeImagePath(event.imagePath)
            is VehiculoEvent.OnChangeProveedorId -> onChangeProveedorId(event.proveedorId)
            VehiculoEvent.Save -> save()
            VehiculoEvent.GetVehiculos -> getVehiculos()
            is VehiculoEvent.OnChangeAnio -> onChangeAnio(event.anio)
            is VehiculoEvent.OnChangeMarcaId -> onChangeMarcaId(event.marcaId)
            is VehiculoEvent.GetMarca -> getMarca(event.vehiculoId)
        }
    }
}