package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.RentaRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class RentaViewModel @Inject constructor(
    private val rentaRepository: RentaRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(Uistate())
    val uistate = _uistate.asStateFlow()

    init {

    }

    private fun getRentas() {
        viewModelScope.launch {
            rentaRepository.getRentas().collect { result ->

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
                                rentas = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            val renta = rentaRepository.addRenta(uistate.value.toEntity())
            renta.collect { result ->
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
                                success = "Renta agregada"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun nuevo() {
        _uistate.update {
            it.copy(
                rentaId = null,
                clienteId = null,
                vehiculoId = null,
                fechaRenta = null,
                fechaEntrega = null,
                total = null,
                success = "",
                error = "",
            )
        }
    }

    private fun onChangeClienteId(clienteId: Int) {
        _uistate.update {
            it.copy(
                clienteId = clienteId
            )
        }
    }

    private fun onChangeVehiculoId(vehiculoId: Int) {
        _uistate.update {
            it.copy(
                vehiculoId = vehiculoId
            )
        }
    }

    private fun onChangeFechaRenta(fechaRenta: String) {
        _uistate.update {
            it.copy(
                fechaRenta = fechaRenta
            )
        }
    }

    private fun onChangeFechaEntrega(fechaEntrega: String) {
        _uistate.update {
            it.copy(
                fechaEntrega = fechaEntrega
            )
        }
    }

    private fun onChangeTotal(total: Int) {
        _uistate.update {
            it.copy(
                total = total
            )
        }
    }

    fun onEvent(event: RentaEvent) {
        when (event) {
            is RentaEvent.OnchangeClienteId -> onChangeClienteId(event.clienteId)
            is RentaEvent.OnchangeFechaEntrega ->  onChangeFechaEntrega(event.fechaEntrega)
            is RentaEvent.OnchangeFechaRenta -> onChangeFechaRenta(event.fechaRenta)
            is RentaEvent.OnchangeTotal -> onChangeTotal(event.total)
            is RentaEvent.OnchangeVehiculoId -> onChangeVehiculoId(event.vehiculoId)
            RentaEvent.Save -> save()
        }
    }

}
