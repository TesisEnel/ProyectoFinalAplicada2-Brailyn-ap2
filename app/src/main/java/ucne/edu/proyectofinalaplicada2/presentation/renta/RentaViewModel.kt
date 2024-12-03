package ucne.edu.proyectofinalaplicada2.presentation.renta

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.repository.ModeloRepository
import ucne.edu.proyectofinalaplicada2.repository.RentaRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import ucne.edu.proyectofinalaplicada2.repository.TipoVehiculoRepository
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
    private val tipoCombustibleRepository: TipoCombustibleRepository,
    private val tipoVehiculoRepository: TipoVehiculoRepository,
) : ViewModel() {
    private val _uistate = MutableStateFlow(RentaUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getRentas()
    }

    fun getRentas() {
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
                       // mostrarDatosVehiculo()
                    }
                }
            }
        }
    }

    fun save(rentaDto: RentaDto) {
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

                val vehiculo = getVehiculoById(rentaEntity.vehiculoId ?: 0)
                val marca = getMarcaById(vehiculo?.marcaId ?: 0)
                val modelo = getModeloById(vehiculo?.modeloId ?: 0)
                RentaConVehiculo(
                    marca = marca,
                    renta = rentaEntity,
                    nombreModelo = modelo?.modeloVehiculo,
                    anio = vehiculo?.anio
                )
            }
            _uistate.update {
                it.copy(
                    rentaConVehiculos = rentaConVehiculo
                )
            }

        }
    }
    private fun mostrarDatosVehiculoByRole(isAdmin: Boolean) {
        if(!isAdmin){
            val email = FirebaseAuth.getInstance().currentUser?.email

            viewModelScope.launch {
                val user = getClienteByEmail(email ?: "")
                val rentaConVehiculo = _uistate.value.rentas.filter { rentaEntity ->
                    rentaEntity.clienteId == user?.clienteId
                }.map { rentaEntity ->
                    val vehiculo = getVehiculoById(rentaEntity.vehiculoId ?: 0)
                    val marca = getMarcaById(vehiculo?.marcaId ?: 0)
                    val modelo = getModeloById(vehiculo?.modeloId ?: 0)
                    RentaConVehiculo(
                        marca = marca,
                        renta = rentaEntity,
                        nombreModelo = modelo?.modeloVehiculo,
                        anio = vehiculo?.anio
                    )
                }

                _uistate.update {
                    it.copy(
                        rentaConVehiculos = rentaConVehiculo
                    )
                }
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
            val vehiculo = getVehiculoById(vehiculoId)
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
            val modelo = getModeloById(uistate.value.vehiculo?.modeloId ?: 0)
            if (modelo != null) {
                _uistate.update {
                    it.copy(
                        modelo = modelo
                    )
                }
            }
            val tipoCombustible = getCombustibleById(uistate.value.vehiculo?.tipoCombustibleId ?: 0)
            if (tipoCombustible != null) {
                _uistate.update {
                    it.copy(
                        tipoCombustibleEntity = tipoCombustible
                    )
                }
            }
            val tipoVehiculo = getTipoVehiculoById(uistate.value.vehiculo?.tipoVehiculoId ?: 0)

            if (tipoVehiculo != null) {
                _uistate.update {
                    it.copy(
                        tipoVehiculoEntity = tipoVehiculo
                    )
                }
            }
            _uistate.update {
                it.copy(
                    vehiculoNombre = uistate.value.marca?.nombreMarca,
                    vehiculoId = uistate.value.vehiculo?.vehiculoId,
                    vehiculoModelo = uistate.value.modelo?.modeloVehiculo,
                    vehiculoConCombustible = uistate.value.tipoCombustibleEntity?.nombreTipoCombustible,
                    vehiculoConTipo = uistate.value.tipoVehiculoEntity?.nombreTipoVehiculo
                )
            }
        }
    }
    suspend fun getVehiculoById(id: Int): VehiculoEntity? {
        return vehiculoRepository.getVehiculoById(id).data
    }
    suspend fun getClienteByEmail(email: String): ClienteDto? {
        return clienteRepository.getClienteByEmail(email).data
    }



     suspend fun getMarcaById(id: Int): MarcaEntity? {
        return marcaRepository.getMarcaById(id).data
    }
     suspend fun getModeloById(id: Int): ModeloEntity? {
        return modeloRepository.getModelosById(id).data
    }
     suspend fun getCombustibleById(id: Int): TipoCombustibleEntity? {
        return tipoCombustibleRepository.getTipoCombustibleById(id).data
    }
     suspend fun getTipoVehiculoById(id: Int): TipoVehiculoEntity? {
        return tipoVehiculoRepository.getTipoVehiculoById(id).data
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
                fechaRenta = fechaRenta,
                errorFechaRenta = null
            )
        }
    }
    private fun onChangeFechaEntrega(fechaEntrega: String) {
        _uistate.update {
            it.copy(
                fechaEntrega = fechaEntrega,
                errorFechaEntrega = null
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
        val today = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())

        if (fechaRenta.isNullOrBlank()) {
            _uistate.update {
                it.copy(
                    total = null,
                    errorFechaRenta = "Ingrese la fecha de renta",
                    showModal = false
                )
            }
            return
        }
        if (fechaEntrega.isNullOrBlank()) {
            _uistate.update {
                it.copy(
                    total = null,
                    errorFechaEntrega = "Ingrese la fecha de entrega",
                    showModal = false
                )
            }
            return
        }
        if (fechaRenta == fechaEntrega) {
            _uistate.update {
                it.copy(
                    total = null,
                    error = "Las fechas de renta y entrega no pueden ser iguales",
                    showModal = false
                )
            }
            return
        }
        if (fechaRenta > fechaEntrega) {
            _uistate.update {
                it.copy(
                    total = null,
                    error = "La fecha de renta no puede ser mayor a la fecha de entrega",
                    showModal = false
                )
            }
            return
        }
        if (fechaRenta < today) {
            _uistate.update {
                it.copy(
                    total = null,
                    errorFechaRenta = "La fecha de renta no puede ser anterior a hoy",
                    showModal = false
                )
            }
            return
        }

        try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val rentaDate: Date = dateFormat.parse(fechaRenta) ?: throw Exception("Fecha de renta inválida")
            val entregaDate: Date = dateFormat.parse(fechaEntrega) ?: throw Exception("Fecha de entrega inválida")

            if (entregaDate.before(rentaDate)) {
                _uistate.update {
                    it.copy(
                        total = null,
                        error = "La fecha de entrega no puede ser anterior a la fecha de renta",
                        showModal = false
                    )
                }
                return
            }
            val diffInMillis = entregaDate.time - rentaDate.time
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt() + 1
            val total = diffInDays * costoDiario
            _uistate.update {
                it.copy(
                    total = total.toDouble(),
                    cantidadDias = diffInDays,
                    error = null,
                    errorFechaRenta = null,
                    errorFechaEntrega = null,
                    showModal = true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _uistate.update {
                it.copy(
                    total = null,
                    error = "Ocurrió un error al procesar las fechas",
                    showModal = false
                )
            }
        }
    }

    private fun closeModal() {
            _uistate.update {
                it.copy(
                    showModal = false
                )
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
            RentaEvent.CloseModal -> closeModal()
            is RentaEvent.MostraDatosVehiculoByRole -> mostrarDatosVehiculoByRole(event.isAdmin)
            RentaEvent.MostraDatosVehiculo -> mostrarDatosVehiculo()
        }
    }
}
