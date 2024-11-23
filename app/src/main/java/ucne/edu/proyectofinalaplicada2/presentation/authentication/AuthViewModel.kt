package ucne.edu.proyectofinalaplicada2.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ClienteDto
import ucne.edu.proyectofinalaplicada2.presentation.cliente.Uistate
import ucne.edu.proyectofinalaplicada2.presentation.cliente.toEntity
import ucne.edu.proyectofinalaplicada2.repository.AuthRepository
import ucne.edu.proyectofinalaplicada2.repository.ClienteRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository,
    private val authRepository: AuthRepository
):ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _uistateCliente = MutableStateFlow(Uistate())
    val uistateCliente = _uistateCliente.asStateFlow()

    private val _uistate = MutableStateFlow(UiState())
    val uistate = _uistate.asStateFlow()


    init {
        checkAuthStatus()
    }
    private fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }
    //Inicio de sesion
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
    private fun signup(): Boolean {
        var isSuccessful = false

        if (!validar()) {
            return isSuccessful
        }
        viewModelScope.launch {
            authRepository.signup(uistate.value.email, uistate.value.password)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uistate.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            saveCliente(uistateCliente.value.toEntity())
                            _uistate.update { it.copy(isLoading = false, error = null) }
                            isSuccessful = true // Marcar éxito en Firebase
                        }
                        is Resource.Error -> {
                            _uistate.update { it.copy(isLoading = false, error = "Este email ya existe") }
                        }
                    }
                }

        }
        return isSuccessful
    }

    private fun signout(){
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
            )
        }
        return !error
    }


    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})$".toRegex()
        return emailRegex.matches(email)
    }


    private fun onEmailChanged(email : String) {
        _uistate.value = _uistate.value.copy(
            email = email,
            error = null
        )
    }
    private fun onPasswordChanged(password : String) {
        _uistate.value = _uistate.value.copy(
            password = password
        )
    }

    private fun saveCliente(cliente: ClienteDto) {
        try {
            clienteRepository.addCliente(cliente)
        } catch (e: Exception) {
            _uistate.update { it.copy(error = "Error al guardar los datos del cliente: ${e.message}") }
        }
    }

     fun onEvent(event: AuthEvent){
        when(event){
            AuthEvent.Login -> login()
            AuthEvent.Signup -> signup()
            AuthEvent.Signout -> signout()
            is AuthEvent.OnChangeEmail -> onEmailChanged(event.email)
            is AuthEvent.OnChangePassword -> onPasswordChanged(event.password)
        }
    }


}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

