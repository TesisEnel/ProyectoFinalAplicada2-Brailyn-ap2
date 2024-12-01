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

    private fun getListVehiculosByMarcaId(marcaId: Int) {
        viewModelScope.launch {
            vehiculoRepository.getListVehiculosByMarcaId(marcaId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                isLoading = false,
                                vehiculos = result.data ?: emptyList()
                            )
                        }
                        getModeloConVehiculos(marcaId)
                    }
                }
            }
        }

    }

    private fun getModeloConVehiculos(marcaId: Int) {
        viewModelScope.launch {
            val marca = marcaRepository.getMarcaById(marcaId).data
            val modeloConVehiculos = _uistate.value.vehiculos.map { vehiculoEntity ->
                val modelo = modeloRepository.getModelosById(vehiculoEntity.modeloId?:0).data
                ModeloConVehiculo(
                    nombreModelo = modelo?.modeloVehiculo?:"",
                    vehiculo = vehiculoEntity,
                    marca = marca
                )
            }
            _uistate.update {
                it.copy(
                    modeloConVehiculos = modeloConVehiculos,
                )
            }
        }
    }

    fun onEvent(event: ModeloEvent) {
        when (event) {
            is ModeloEvent.GetModeloConVehiculos -> {
                getModeloConVehiculos(event.marcaId)
            }
            is ModeloEvent.GetVehiculosByMarcaId -> {
                getListVehiculosByMarcaId(event.marcaId)
            }
        }
    }
}
