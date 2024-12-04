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
import ucne.edu.proyectofinalaplicada2.data.local.entities.ClienteEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.MarcaEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.ModeloEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoCombustibleEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.TipoVehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
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
                    }
                }
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            var vehiculo = getVehiculoById(uistate.value.vehiculoId ?: 0)
            vehiculo = vehiculo?.copy(estaRentado = true)
            val renta = rentaRepository.addRenta(_uistate.value.toDto(),vehiculo)
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
                val cliente = clienteRepository.getClienteById(rentaEntity.clienteId?:0)
                RentaConVehiculo(
                    marca = marca,
                    renta = rentaEntity,
                    nombreModelo = modelo?.modeloVehiculo,
                    anio = vehiculo?.anio,
                    clienteEntity = cliente.data
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

    fun prepareRentaData(emailCliente: String?, vehiculoId: Int, rentaId: Int) {
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
            val today = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
            val renta = _uistate.value.rentas.find { it.rentaId == rentaId }
            if(rentaId>0){
                _uistate.update {
                    it.copy(
                        rentaId = rentaId,
                        fechaEntrega = renta?.fechaEntrega,
                        total = renta?.total,
                        fechaRenta = renta?.fechaRenta?:"",
                        vehiculoId = renta?.vehiculoId,
                        clienteId = renta?.clienteId,
                        vehiculoNombre = uistate.value.marca?.nombreMarca,
                        vehiculoModelo = uistate.value.modelo?.modeloVehiculo,
                        vehiculoConCombustible = uistate.value.tipoCombustibleEntity?.nombreTipoCombustible,
                        vehiculoConTipo = uistate.value.tipoVehiculoEntity?.nombreTipoVehiculo,
                        renta = renta
                    )
                }
            }else{
                _uistate.update {
                    it.copy(
                        vehiculoNombre = uistate.value.marca?.nombreMarca,
                        vehiculoId = uistate.value.vehiculo?.vehiculoId,
                        vehiculoModelo = uistate.value.modelo?.modeloVehiculo,
                        vehiculoConCombustible = uistate.value.tipoCombustibleEntity?.nombreTipoCombustible,
                        vehiculoConTipo = uistate.value.tipoVehiculoEntity?.nombreTipoVehiculo,
                        fechaRenta = today
                    )
                }
            }

        }
    }

    suspend fun getVehiculoById(id: Int): VehiculoEntity? {
        return vehiculoRepository.getVehiculoById(id).data
    }

    suspend fun getClienteByEmail(email: String): ClienteEntity? {
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


    private fun updateRenta() {
        viewModelScope.launch {
            rentaRepository.updateRenta(uistate.value.toDto()).collect { result ->
                when (result) {
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
                                success = "Renta actualizada",
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleDatePickerResult(dateMillis: Long?, isStartDate: Boolean) {
        dateMillis?.let { millis ->
            val selectedDate =
                SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(millis))
            if (isStartDate) {
                _uistate.update {
                    it.copy(
                        fechaRenta = selectedDate,
                        errorFechaRenta = null
                    )
                }
            } else {
                _uistate.update {
                    it.copy(
                        fechaEntrega = selectedDate,
                        errorFechaEntrega = null
                    )
                }
            }
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
            val rentaDate: Date =
                dateFormat.parse(fechaRenta) ?: throw Exception("Fecha de renta inválida")
            val entregaDate: Date =
                dateFormat.parse(fechaEntrega) ?: throw Exception("Fecha de entrega inválida")

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
            val costoAdicional = calcularCostoAdicional(uistate.value.renta?.fechaEntrega?:"", uistate.value.fechaEntrega?:"", costoDiario.toDouble())
            if (diffInDays < 3) {
                _uistate.update {
                    it.copy(
                        total = null,
                        error = "El periodo de renta debe ser de al menos 3 días",
                        showModal = false,
                    )
                }
                return
            }
            val total = diffInDays * costoDiario

            _uistate.update {
                it.copy(
                    total = total.toDouble(),
                    cantidadDias = diffInDays,
                    costoAdicional = costoAdicional,
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

    private fun calcularCostoAdicional(fechaEntrega: String, newFecha: String,precio: Double):Double{
        if(fechaEntrega.isBlank() || newFecha.isBlank()){
            return 0.0
        }

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val entregaDate: Date = dateFormat.parse(fechaEntrega) ?: return 0.0
        val newDate: Date = dateFormat.parse(newFecha) ?: return 0.0

        val diffInMillis = newDate.time - entregaDate.time

        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

        return if (diffInDays > 0) {
            diffInDays * precio
        } else {
            0.0
        }
    }

    private fun selectedRenta(vehiculoId: Int) {
        viewModelScope.launch {
            val renta = _uistate.value.rentas.find { it.vehiculoId == vehiculoId }
            val vehiculo = getVehiculoById(vehiculoId)
            val marca = getMarcaById(vehiculo?.marcaId ?: 0)
            val tipoCombustible = getCombustibleById(vehiculo?.tipoCombustibleId ?: 0)
            val tipoVehiculo = getTipoVehiculoById(vehiculo?.tipoVehiculoId ?: 0)
            val modelo = getModeloById(vehiculo?.modeloId ?: 0)
            val today = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
            _uistate.update {
                it.copy(
                    fechaRenta = today,
                    fechaEntrega = renta?.fechaEntrega,
                    total = renta?.total,
                    rentaId = renta?.rentaId,
                    vehiculoNombre = uistate.value.vehiculoNombre,
                    vehiculoModelo = uistate.value.vehiculoModelo,
                    vehiculoId = vehiculo?.vehiculoId,
                    marca = marca,
                    modelo = modelo,
                    vehiculoConCombustible = tipoCombustible?.nombreTipoCombustible,
                    vehiculoConTipo = tipoVehiculo?.nombreTipoVehiculo,
                )
            }
        }

    }

    private fun deleteRenta(rentaId: Int, vehiculoId: Int) {
        viewModelScope.launch {
            rentaRepository.deleteRenta(rentaId, vehiculoId).collect{result->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = result.message ?: "Error",
                                isDataLoading = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uistate.update {
                            it.copy(
                                isDataLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                success = "Renta eliminada",
                                isDataLoading = false
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
                fechaEntrega = "",
                total = null,
                error = null,
                errorFechaRenta = null,
                errorFechaEntrega = null,
            )
        }
    }

    private fun closeModal() {
        _uistate.update {
            it.copy(
                showModal = false
            )
        }
    }
    private fun clearSuccess() {
        _uistate.update {
            it.copy(
                success = ""
            )
        }
    }private fun clearError() {
        _uistate.update {
            it.copy(
                success = ""
            )
        }
    }

    fun onEvent(event: RentaEvent) {
        when (event) {
            RentaEvent.ConfirmRenta -> save()
            RentaEvent.CloseModal -> closeModal()
            RentaEvent.MostraDatosVehiculo -> mostrarDatosVehiculo()
            RentaEvent.Nuevo -> nuevo()
            RentaEvent.UpdateRenta -> updateRenta()
            RentaEvent.ClearSuccess -> clearSuccess()
            RentaEvent.ClearError -> clearError()
            is RentaEvent.OnchangeClienteId -> onChangeClienteId(event.clienteId)
            is RentaEvent.OnchangeFechaEntrega -> onChangeFechaEntrega(event.fechaEntrega)
            is RentaEvent.OnchangeFechaRenta -> onChangeFechaRenta(event.fechaRenta)
            is RentaEvent.OnchangeTotal -> onChangeTotal(event.total)
            is RentaEvent.OnchangeVehiculoId -> onChangeVehiculoId(event.vehiculoId)
            is RentaEvent.CalculeTotal -> calculateTotal(
                event.fechaRenta,
                event.fechaEntrega,
                event.costoDiario
            )
            is RentaEvent.PrepareRentaData -> prepareRentaData(event.emailCliente, event.vehiculoId,event.rentaId)
            is RentaEvent.MostraDatosVehiculoByRole -> mostrarDatosVehiculoByRole(event.isAdmin)
            is RentaEvent.HandleDatePickerResult -> handleDatePickerResult(
                event.dateMillis,
                event.isStartDate
            )

            is RentaEvent.SelectedRenta -> selectedRenta(event.vehiculoId)
            is RentaEvent.DeleteRenta -> deleteRenta(event.rentaId, event.vehiculoId)
            is RentaEvent.GetRentas -> getRentas()

        }
    }
}
