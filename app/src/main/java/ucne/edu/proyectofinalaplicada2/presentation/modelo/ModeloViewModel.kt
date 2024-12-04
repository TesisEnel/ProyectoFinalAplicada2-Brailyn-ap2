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

    private var selectedFilter: ModeloFilter = ModeloFilter.Todos

    private fun getListVehiculosByMarcaId(marcaId: Int?=null, isAdmin:Boolean ) {
        viewModelScope.launch {
            if(_uistate.value.isDataLoaded){
                return@launch
            }
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
                                vehiculos = result.data ?: emptyList(),
                                isDataLoaded = true
                            )
                        }
                        getModeloConVehiculos(marcaId,isAdmin)
                    }
                }
            }
        }

    }

    private fun getModeloConVehiculos(marcaId: Int?, isAdmin:Boolean) {
        viewModelScope.launch {
            val marca = marcaRepository.getMarcaById(marcaId).data
            val modeloConVehiculos = _uistate.value.vehiculos.map { vehiculoEntity ->
                val modelo = modeloRepository.getModelosById(vehiculoEntity.modeloId?:0).data
                ModeloConVehiculo(
                    nombreModelo = modelo?.modeloVehiculo?:"",
                    vehiculo = vehiculoEntity,
                    marca = marca,
                    estaRentado = vehiculoEntity.estaRentado?:false
                )
            }
            val modelosFiltrados = modeloConVehiculos.filter { !it.estaRentado }
            if(isAdmin){
                _uistate.update {
                    it.copy(
                        modeloConVehiculos = modeloConVehiculos,
                        listaFiltrada = filterModelos(modeloConVehiculos),
                        marca = marca
                    )
                }
            }else{
                _uistate.update {
                    it.copy(
                        modeloConVehiculos = modelosFiltrados,
                        listaFiltrada = filterModelos(modelosFiltrados),
                        marca = marca
                    )
                }
            }
        }
    }

    private fun filterModelos(modeloConVehiculos: List<ModeloConVehiculo>): List<ModeloConVehiculo> {
        return when (selectedFilter) {
            is ModeloFilter.Todos -> modeloConVehiculos
            is ModeloFilter.Disponible -> modeloConVehiculos.filter { !it.estaRentado }
            is ModeloFilter.NoDisponible -> modeloConVehiculos.filter { it.estaRentado }
        }
    }

    private fun setFilter(filter: ModeloFilter) {
        selectedFilter = filter
        _uistate.update {
            it.copy(
                listaFiltrada = filterModelos(it.modeloConVehiculos)
            )
        }
    }
    fun onEvent(event: ModeloEvent) {
        when (event) {
            is ModeloEvent.GetModeloConVehiculos -> {
                getModeloConVehiculos(event.marcaId, event.isAdmin)
            }
            is ModeloEvent.GetVehiculosByMarcaId -> {
                getListVehiculosByMarcaId(event.marcaId, event.isAdmin)
            }
            is ModeloEvent.SetFilter -> {
                setFilter(event.filter)
            }

        }
    }
}

sealed class ModeloFilter {
    data object Todos : ModeloFilter()
    data object Disponible : ModeloFilter()
    data object NoDisponible : ModeloFilter()
}
