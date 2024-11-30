package ucne.edu.proyectofinalaplicada2.presentation.renta

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
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
    private val clienteRepository: ClienteRepository,
    private val marcaRepository: MarcaRepository,
    private val modeloRepository: ModeloRepository,
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
                                rentas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        mostrarDatosVehiculo()
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

    private fun mostrarDatosVehiculo() {
        viewModelScope.launch {
            val rentaConVehiculo = _uistate.value.rentas.map { rentaEntity ->
                val vehiculo = getVehiculoById(rentaEntity.vehiculoId?:0)
                val marca = getMarcaById(vehiculo?.marcaId ?: 0)
                val modelo = getModeloById(vehiculo?.modeloId?:0)
                RentaConVehiculo(
                    marca = marca,
                    renta = rentaEntity,
                    nombreModelo = modelo?.modeloVehiculo,
                    anio = vehiculo?.anio
                )
            }
            _uistate.update {
                it.copy(
                    rentaConVehiculo = rentaConVehiculo
                )
            }

        }
    }

    private fun prepareRentaData(emailCliente: String?, vehiculoId: Int) {
        viewModelScope.launch {

            val cliente = getClienteByEmail(emailCliente ?: "")
            if (cliente != null) {
                _uistate.update {
                    it.copy(
                        clienteId = cliente.clienteId
                    )
                }
            }
            val vehiculo = getvehiculoById(vehiculoId)
            if (vehiculo != null) {
                _uistate.update {
                    it.copy(
                        vehiculo = vehiculo
                    )
                }
            }
            val marca = getMarcaById(uistate.value.vehiculo?.marcaId ?: 0)
            if (marca != null) {
                _uistate.update {
                    it.copy(
                        marca = marca
                    )
                }
            }
            _uistate.update {
                it.copy(
                    vehiculoNombre = uistate.value.marca?.nombreMarca,
                    vehiculoId = uistate.value.vehiculo?.vehiculoId,
                )
            }
        }
    }
    private suspend fun getVehiculoById(id: Int): VehiculoEntity?{
       return vehiculoRepository.getVehiculoById(id).data
    }
    private suspend fun getClienteByEmail(email: String): ClienteDto? {
        return clienteRepository.getClienteByEmail(email).data
    }

    private suspend fun getvehiculoById(id: Int): VehiculoEntity? {
        return vehiculoRepository.getVehiculoById(id).data
    }

    private suspend fun getMarcaById(id: Int): MarcaEntity? {
        return marcaRepository.getMarcaById(id).data
    }
    private suspend fun getModeloById(id: Int): ModeloEntity? {
        return modeloRepository.getModelosById(id).data
    }

    private fun createRenta() {
        val renta = RentaDto(
            clienteId = uistate.value.clienteId,
            vehiculoId = uistate.value.vehiculo?.vehiculoId,
            fechaRenta = uistate.value.fechaRenta,
            fechaEntrega = uistate.value.fechaEntrega,
            total = uistate.value.total
        )
        save(renta)
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

    private fun onChangeTotal(total: Double) {
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
                onChangeTotal(total.toDouble())
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
            is RentaEvent.OnchangeFechaEntrega -> onChangeFechaEntrega(event.fechaEntrega)
            is RentaEvent.OnchangeFechaRenta -> onChangeFechaRenta(event.fechaRenta)
            is RentaEvent.OnchangeTotal -> onChangeTotal(event.total)
            is RentaEvent.OnchangeVehiculoId -> onChangeVehiculoId(event.vehiculoId)
            is RentaEvent.Save -> save(event.renta)
            is RentaEvent.CalculeTotal -> calculateTotal(
                event.fechaRenta,
                event.fechaEntrega,
                event.costoDiario
            )

            is RentaEvent.PrepareRentaData -> prepareRentaData(event.emailCliente, event.vehiculoId)
            RentaEvent.ConfirmRenta -> createRenta()
        }
    }

}
