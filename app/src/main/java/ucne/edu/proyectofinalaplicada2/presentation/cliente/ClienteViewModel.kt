package ucne.edu.proyectofinalaplicada2.presentation.cliente

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    private val _uistate = MutableStateFlow(ClienteUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getClientes()
    }

    private fun getClientes() {
        viewModelScope.launch {
            clienteRepository.getClientes().collect { result ->
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
                                clientes = result.data ?: emptyList()

                            )
                        }
                    }
                }
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            if (validar()) {
                val cliente = clienteRepository.addCliente(uistate.value.toEntity())
                cliente.collect { result ->
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
                                    success = "Cliente agregado"
                                )
                            }
                            nuevo()
                        }
                    }
                }
            }
        }
    }

    private fun update(id: Int) {
        viewModelScope.launch {

            val cliente =
                clienteRepository.updateCliente(id, uistate.value.toEntity())

            cliente.collect { result ->
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
                                success = "Cliente actualizado"
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
                clienteId = null,
                cedula = "",
                nombre = "",
                apellidos = "",
                direccion = "",
                celular = "",
                success = "",
                error = "",
            )
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun validar(): Boolean {
        var error = false
        _uistate.update {
            it.copy(
                errorCelular = if (it.celular.isBlank() || !isValidPhone(it.celular)) {
                    error = true
                    if (it.celular.isBlank()) "El celular no puede estar vacio" else
                    "El número de celular no es válido ej 8299440000"
                } else "",
                errorCedula = if (it.cedula.isBlank() || !isValidCedula(it.cedula)) {
                    error = true
                    if (it.cedula.isBlank()) "La cedula no puede estar vacia" else
                        "La cedula no es valida"
                } else "",
                errorNombre = if (it.nombre.isBlank()) {
                    error = true
                    "El nombre no puede estar vacio"
                } else "",
                errorApellidos = if (it.apellidos.isBlank()) {
                    error = true
                    "El apellido no puede estar vacios"
                } else "",
                errorDireccion = if (it.direccion.isBlank()) {
                    error = true
                    "La direccion no puede estar vacia"
                } else ""
            )
        }
        return !error
    }

    private fun isValidPhone(phone: String): Boolean {
        if (phone.length != 10 || !phone.all { it.isDigit() }) return false
        val dominicanPrefixes = listOf("809", "829", "849")
        val usPrefixes = (2..9).map { it.toString() }
        val prefix = phone.substring(0, 3)

        return prefix in dominicanPrefixes || prefix[0].toString() in usPrefixes
    }

    private fun isValidCedula(cedula: String): Boolean {
        return !(cedula.length != 11 || !cedula.all { it.isDigit() })
    }
    private fun onChangeCedula(cedula: String) {
        _uistate.update {
            it.copy(
                cedula = cedula,
                errorCedula = ""
            )
        }
    }

    private fun onChangeNombre(nombre: String) {
        _uistate.update {
            it.copy(
                nombre = nombre,
                errorNombre = ""
            )
        }
    }

    private fun onChangeApellidos(apellidos: String) {
        _uistate.update {
            it.copy(
                apellidos = apellidos,
                errorApellidos = ""
            )
        }
    }

    private fun onChangeDireccion(direccion: String) {
        _uistate.update {
            it.copy(
                direccion = direccion
                , errorDireccion = ""
            )
        }
    }

    private fun onChangeCelular(celular: String) {
        _uistate.update {
            it.copy(
                celular = celular,
                errorCelular = ""
            )
        }
    }

    private fun onChangeEmail(email: String) {
        _uistate.update {
            it.copy(
                email = email,
                errorEmail = ""
            )
        }
    }

    fun onEvent(event: ClienteEvent) {
        when (event) {
            is ClienteEvent.OnchangeApellidos -> onChangeApellidos(event.apellidos)
            is ClienteEvent.OnchangeCedula -> onChangeCedula(event.cedula)
            is ClienteEvent.OnchangeCelular -> onChangeCelular(event.celular)
            is ClienteEvent.OnchangeDireccion -> onChangeDireccion(event.direccion)
            is ClienteEvent.OnchangeNombre -> onChangeNombre(event.nombre)
            ClienteEvent.Save -> save()
            ClienteEvent.Update -> TODO()
            is ClienteEvent.OnChangeEmail -> onChangeEmail(event.email)
        }
    }
}
