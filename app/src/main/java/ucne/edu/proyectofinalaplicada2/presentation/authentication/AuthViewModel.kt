package ucne.edu.proyectofinalaplicada2.presentation.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.repository.AuthRepository
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository,
    private val authRepository: AuthRepository,
    private val googleAuthClient: GoogleAuthClient,
) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()

    private val _uistate = MutableStateFlow(ClienteUiState())
    val uistate = _uistate.asStateFlow()


    init {
        checkAuthStatus()
        getClientes()
    }

    private fun signInWithGoogle() {
        viewModelScope.launch {
            _uistate.update { it.copy(isLoading = true) }

            try {
                val user = googleAuthClient.signInAndGetUser()
                if (user != null) {
                    handleUserSignIn(user)
                } else {
                    _uistate.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo iniciar sesión."
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uistate.update { it.copy(isLoading = false, error = "Error de autenticación.") }
            }
        }
    }

    private fun getClientes() {
        viewModelScope.launch {
            clienteRepository.getClientes().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update { it.copy(error = it.error) }
                    }

                    is Resource.Loading -> {
                        _uistate.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _uistate.update { it.copy(clientes = result.data?: emptyList(), isLoading = false) }
                    }
                }
            }
        }
    }

     private fun handleUserSignIn(user: FirebaseUser) {
        viewModelScope.launch {
            val clienteExiste = clienteExist( user.email?:"")
            if (clienteExiste) {
                _uistate.update { it.copy(success = "Bienvenido de nuevo.") }
                return@launch
            }

            val clienteDto = ClienteDto(
                clienteId = null,
                cedula = "",
                nombre = user.displayName ?: "Usuario de Google",
                apellido = "",
                direccion = "",
                celular = "",
                email = user.email ?: "",
                isAdmin = true
            )
            clienteRepository.addCliente(clienteDto).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uistate.update {
                            it.copy(
                                isLoading = false,
                                success = "Cliente registrado exitosamente."
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al registrar cliente: ${resource.message}"
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uistate.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
    private fun clienteExist(email: String): Boolean {
        return try {
            clienteRepository.clienteNotExist(email, _uistate.value.clientes)
        } catch (e: Exception) {
            false
        }
    }
    private fun isAdminUser(email: String): Boolean {
        return try {
            clienteRepository.isAdminUser(email, _uistate.value.clientes)
        } catch (e: Exception) {
            false
        }
    }
    private fun checkIfUserIsAdmin(email: String) {
        viewModelScope.launch {
            val isAdmin = isAdminUser(email)
            _uistate.update { it.copy(isAdmin = isAdmin) }
        }
    }
    private fun updateUsuario(emailUsuario: String?) {
        viewModelScope.launch {
            // Obtener los datos del cliente desde la API
            val cliente = getClienteByEmail(emailUsuario ?: "")
            if (cliente != null) {
                // Actualizar el estado con los datos obtenidos
                _uistate.update {
                    it.copy(
                        clienteId = cliente.clienteId,
                        nombre = cliente.nombre ?: "",
                        email = cliente.email ?: "",
                        apellidos = cliente.apellido ?: "",
                        cedula = cliente.cedula ?: "",
                        celular = cliente.celular ?: "",
                        direccion = cliente.direccion ?: "",
                    )
                }
            }


        }
    }

    private fun uppdateClient(){
        if (!validarSettings()) return
        viewModelScope.launch {

                clienteRepository.updateCliente(uistate.value.clienteId?:0, uistate.value.toEntity()).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uistate.update { it.copy(isLoading = true) }
                        }
                        is Resource.Success -> {
                            _uistate.update {
                                it.copy(
                                    isLoading = false,
                                    success = "Usuario actualizado correctamente."
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uistate.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Error al actualizar usuario: ${result.message}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getClienteByEmail(email: String): ClienteDto? {
        return clienteRepository.getClienteByEmail(email).last().data
    }


    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }


    private fun login() {
        viewModelScope.launch {
            authRepository.login(uistate.value.email, uistate.value.password)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uistate.update {
                                it.copy(isLoading = true, error = null)
                            }
                        }

                        is Resource.Success -> {
                            _uistate.update {
                                it.copy(isLoading = false, error = null)
                            }
                        }

                        is Resource.Error -> {
                            _uistate.update {
                                it.copy(isLoading = false, error = result.message)
                            }
                        }
                    }
                }
        }
    }

    private fun signup() {
        if (!validar()) return

        viewModelScope.launch {
            authRepository.signup(uistate.value.email, uistate.value.password)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _uistate.update {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }

                        is Resource.Success -> {
                            saveCliente()
                            _uistate.update { it.copy(isLoading = false, error = null) }
                            nuevo()
                        }

                        is Resource.Error -> {
                            _uistate.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Este email ya existe"
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    private fun validar(): Boolean {
        var error = false
        _uistate.update {
            it.copy(
                errorEmail = if (it.email.isBlank() || !isValidEmail(it.email)) {
                    error = true
                    if (it.email.isBlank()) "El email no puede estar vacío"
                    else if (!isValidEmail(it.email)) "El formato del email no es válido"
                    else null
                } else null,
                errorPassword = if (uistate.value.password.isBlank()) {
                    error = true
                    "La contraseña es requerida"
                } else if (uistate.value.password.length < 6) {
                    error = true
                    "La contraseña debe tener al menos 6 caracteres"
                } else null,
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
    private fun validarSettings(): Boolean {
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

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})$".toRegex()
        return emailRegex.matches(email)
    }


    private fun onEmailChanged(email: String) {
        _uistate.value = _uistate.value.copy(
            email = email,
            error = null
        )
    }

    private fun onPasswordChanged(password: String) {
        _uistate.value = _uistate.value.copy(
            password = password
        )
    }

    private fun saveCliente() {
        viewModelScope.launch {
            clienteRepository.addCliente(uistate.value.toEntity()).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _uistate.update {
                            it.copy(
                                error = "No se pudo guardar",
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
                                success = "Se guardó correctamente",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
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
                direccion = direccion, errorDireccion = ""
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
                isLoading = false,
                email = "",
                password = "", errorEmail = null,
                errorPassword = null,
                errorCelular = "",
                errorCedula = "",
                errorNombre = "",
                errorApellidos = "",
                errorDireccion = ""


            )
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.Login -> login()
            AuthEvent.Signup -> signup()
            AuthEvent.SaveCliente -> saveCliente()
            AuthEvent.Signout -> signout()
            is AuthEvent.OnChangeEmail -> onEmailChanged(event.email)
            is AuthEvent.OnChangePassword -> onPasswordChanged(event.password)

            AuthEvent.SignInWithGoogle -> signInWithGoogle()
            is AuthEvent.OnchangeApellidos -> onChangeApellidos(event.apellidos)
            is AuthEvent.OnchangeCedula -> onChangeCedula(event.cedula)
            is AuthEvent.OnchangeCelular -> onChangeCelular(event.celular)
            is AuthEvent.OnchangeDireccion -> onChangeDireccion(event.direccion)
            is AuthEvent.OnchangeNombre -> onChangeNombre(event.nombre)
            is AuthEvent.CheckIfUserIsAdmin -> checkIfUserIsAdmin(event.email)
            is AuthEvent.UpdateUsuario -> updateUsuario(event.email)
            is AuthEvent.UpdateClient -> uppdateClient()
            is AuthEvent.CheckIfUserIsAdmin -> TODO()
        }
    }


}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

