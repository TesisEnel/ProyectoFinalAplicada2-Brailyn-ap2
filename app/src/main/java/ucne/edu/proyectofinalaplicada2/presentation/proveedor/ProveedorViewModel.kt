package ucne.edu.proyectofinalaplicada2.presentation.proveedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.ProveedorRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class ProveedorViewModel @Inject constructor(
    private val proveedorRepository: ProveedorRepository,
) : ViewModel() {
    private val _uistate = MutableStateFlow(ProveedorUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getProveedores()
    }

    private fun getProveedores() {
        viewModelScope.launch {
            val proveedores = proveedorRepository.getProveedores().data
            _uistate.update {
                it.copy(
                    proveedores = proveedores ?: emptyList()
                )
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            if (!validar()) return@launch
            proveedorRepository.addProveedor(_uistate.value.toEntity()).collect { result ->
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
                                success = "Proveedor agregado",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onProveedorNombre(nombre: String) {
        _uistate.update {
            it.copy(
                nombre = nombre,
            )
        }
    }

    private fun onProveedorTelefono(telefono: String) {
        _uistate.update {
            it.copy(
                telefono = telefono,
            )
        }
    }

    private fun onProveedorModeloId(proeedorId: Int) {
        _uistate.update {
            it.copy(
                proveedorId = proeedorId,
            )
        }
    }
    private fun clearSuccess(){
        _uistate.update {
            it.copy(
                success = ""
            )
        }
    }

    private fun validar(): Boolean {
        var error = false
        _uistate.update {
            it.copy(
                errorNombre = if (it.nombre.isBlank()) {
                    error = true
                    "El nombre no puede estar vacio"
                } else "",
                errorTelefono = if (it.telefono.isBlank() || !isValidPhone(it.telefono)) {
                    error = true
                    if (it.telefono.isBlank()) "El telefono no puede estar vacio" else
                        "El numero de telefono no es valido"
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

    fun onEvent(event: ProveedorEvent) {
        when (event) {
            is ProveedorEvent.OnChangeProveedorID -> onProveedorModeloId(event.proveedorId)
            is ProveedorEvent.OnChangeNombre -> onProveedorNombre(event.nombre)
            is ProveedorEvent.OnChangeTelefono -> onProveedorTelefono(event.telefono)
            ProveedorEvent.OnSave -> save()
            ProveedorEvent.OnClearSuccess -> clearSuccess()
        }
    }
}
