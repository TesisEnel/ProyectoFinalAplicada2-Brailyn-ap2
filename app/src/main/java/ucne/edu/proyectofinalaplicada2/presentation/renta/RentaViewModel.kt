package ucne.edu.proyectofinalaplicada2.presentation.renta

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val vehiculoRepository: VehiculoRepository,
    private val marcaRepository: MarcaRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(Uistate())
    val uistate = _uistate.asStateFlow()

    init {
        getVehiculos()
        getMarcas()
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
            // Asegúrate de que el formato de la fecha coincida con lo esperado
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

            // Verificar y parsear las fechas
            println("Parsing fechaRenta: $fechaRenta")
            val rentaDate: Date? = fechaRenta.let { dateFormat.parse(it) }

            println("Parsing fechaEntrega: $fechaEntrega")
            val entregaDate: Date? = fechaEntrega.let { dateFormat.parse(it) }

            // Validar y calcular el total
            if (rentaDate != null && entregaDate != null && !entregaDate.before(rentaDate)) {
                val diffInMillis = entregaDate.time - rentaDate.time
                val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt() + 1 // Incluye el último día

                val total = diffInDays * costoDiario
                println("Total calculado: $total")
                onChangeTotal(total)
            } else {
                println("Fechas inválidas: entrega es antes que renta")
                _uistate.update {
                    it.copy(total = null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al calcular el total: ${e.message}")
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
            RentaEvent.Save -> save()
            is RentaEvent.CalculeTotal -> calculateTotal(event.fechaRenta, event.fechaEntrega,event.costoDiario)
        }
    }

}
