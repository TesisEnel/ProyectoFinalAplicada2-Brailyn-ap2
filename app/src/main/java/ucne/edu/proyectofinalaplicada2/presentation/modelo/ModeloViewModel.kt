package ucne.edu.proyectofinalaplicada2.presentation.modelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ModeloViewModel @Inject constructor(
    private val modeloRepository: ModeloRepository,
    private val vehiculoRepository: VehiculoRepository,
    private val marcaRepository: MarcaRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(ModeloUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getModelos()
    }

    private fun getModelos(){
        viewModelScope.launch {
            modeloRepository.getModelos().collect { result ->
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
    private fun getVehiculos(){
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
                                vehiculos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }


    private fun getModeloConVehiculos(marcaId: Int) {
        val contador:Int  = 0
        viewModelScope.launch {
            val modeloConVehiculos = _uistate.value.vehiculos.map {
                val vehiculo = vehiculoRepository.getVehiculoByMarcaId(marcaId).data
                val marca = marcaRepository.getMarcaById(marcaId ).data
                val modelo = modeloRepository.getModeloByMarcaId(marcaId).data
                ModeloConVehiculo(
                    nombreModelo = modelo?.modeloVehiculo?:"",
                    vehiculo = vehiculo,
                    marca = marca
                )
            }
            _uistate.update {
                it.copy(
                    modeloConVehiculos = modeloConVehiculos
                )
            }
        }
    }

    fun onEvent(event: ModeloEvent) {
        when (event) {
            is ModeloEvent.GetModeloConVehiculos -> { getModeloConVehiculos(event.marcaId) }
        }
    }
}
