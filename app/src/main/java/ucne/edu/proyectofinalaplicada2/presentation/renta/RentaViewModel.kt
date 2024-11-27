package ucne.edu.proyectofinalaplicada2.presentation.renta

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.repository.AuthRepository
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.RentaRepository
import ucne.edu.proyectofinalaplicada2.repository.VehiculoRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RentaViewModel @Inject constructor(
    private val rentaRepository: RentaRepository,
    private val vehiucloRepository: VehiculoRepository,
    private val clienteRepository: ClienteRepository,
    private val marcaRepository: MarcaRepository,
) : ViewModel() {
    private val _uistate = MutableStateFlow(RentaUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getRentas()
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

    private fun save(rentaDto: RentaDto) {
        viewModelScope.launch {
            val renta = rentaRepository.addRenta(rentaDto)
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
    private fun getMarcaById(id: Int) {
        viewModelScope.launch {
            marcaRepository.getMarcaById(id).collect { result ->

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
                                marca = result.data
                            )
                        }
                    }
                }
            }
        }
    }
    private fun getVehiculoById(id: Int) {
        viewModelScope.launch {
            vehiucloRepository.getVehiculoById(id).collect { result ->

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
                                vehiculo = result.data
                            )
                        }
                    }
                }
            }
        }

    }
    private fun getClienteByEmail(email: String) {
        viewModelScope.launch {
            clienteRepository.getClienteByEmail(email).collect { result ->

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
                                cliente = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun prepareRentaData(emailCliente: String?, vehiculoId: Int) {
        viewModelScope.launch {
            getClienteByEmail(emailCliente?:"")
            getVehiculoById(vehiculoId)
            getMarcaById(uistate.value.vehiculo?.vehiculoId?:0)

            _uistate.update {
                it.copy(
                    vehiculoNombre = uistate.value.marca?.nombreMarca,
                    clienteId = uistate.value.cliente?.clienteId,
                    vehiculoId = uistate.value.vehiculo?.vehiculoId,
                )
            }
        }
    }
    fun createRenta(): RentaDto? {
        val uistate = _uistate.value
        return if (uistate.fechaRenta.isNotEmpty() && uistate.fechaEntrega != null) {
            RentaDto(
                clienteId = uistate.clienteId,
                vehiculoId = uistate.vehiculoId,
                fechaRenta = uistate.fechaRenta,
                fechaEntrega = uistate.fechaEntrega,
                total = uistate.total
            )
        } else null
    }

    private fun nuevo() {
        _uistate.update {
            it.copy(
                rentaId = null,
                clienteId = null,
                vehiculoId = null,
                fechaRenta = "",
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

    private fun calculateTotal(fechaRenta: String?, fechaEntrega: String?, costoDiario: Int) {
        if (fechaRenta.isNullOrBlank() || fechaEntrega.isNullOrBlank()) {
            _uistate.update {
                it.copy(total = null)
            }
            return
        }

        try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val rentaDate: Date? = fechaRenta.let { dateFormat.parse(it) }

            val entregaDate: Date? = fechaEntrega.let { dateFormat.parse(it) }

            if (rentaDate != null && entregaDate != null && !entregaDate.before(rentaDate)) {
                val diffInMillis = entregaDate.time - rentaDate.time
                val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt() + 1

                val total = diffInDays * costoDiario
                onChangeTotal(total)
            } else {
                _uistate.update {
                    it.copy(total = null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _uistate.update {
                it.copy(total = null)
            }
        }
    }
    fun onEvent(event: RentaEvent) {
        when (event) {
            is RentaEvent.OnchangeClienteId -> onChangeClienteId(event.clienteId)
            is RentaEvent.OnchangeFechaEntrega ->  onChangeFechaEntrega(event.fechaEntrega)
            is RentaEvent.OnchangeFechaRenta -> onChangeFechaRenta(event.fechaRenta)
            is RentaEvent.OnchangeTotal -> onChangeTotal(event.total)
            is RentaEvent.OnchangeVehiculoId -> onChangeVehiculoId(event.vehiculoId)
            is RentaEvent.Save -> save(event.renta)
            is RentaEvent.CalculeTotal -> calculateTotal(event.fechaRenta, event.fechaEntrega,event.costoDiario)
            is RentaEvent.PrepareRentaData -> prepareRentaData(event.emailCliente, event.vehiculoId)
            RentaEvent.ConfirmRenta -> {
                val renta = createRenta()
                renta?.let { save(it) }
            }
        }
    }

}
