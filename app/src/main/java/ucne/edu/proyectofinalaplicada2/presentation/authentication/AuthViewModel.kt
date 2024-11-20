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
        viewModelScope.launch {
            authRepository.signup(uistate.value.email, uistate.value.password, uistate.value.toEntity())
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uistate.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            _uistate.update { it.copy(isLoading = false, error = null) }
                        }
                        is Resource.Error -> {
                            _uistate.update { it.copy(isLoading = false, error = result.message) }
                        }
                    }
                }
        }
    }
    private fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    private fun onCedulaChanged(cedula : String){
        _uistate.value = _uistate.value.copy(
            cedula = cedula
        )

    }
    private fun onNombreChanged(nombre : String) {
        _uistate.value = _uistate.value.copy(
            nombre = nombre
        )
    }
    private fun onApellidosChanged(apellidos : String) {
        _uistate.value = _uistate.value.copy(
            apellidos = apellidos
        )
    }
    private fun onDireccionChanged(direccion : String) {
        _uistate.value = _uistate.value.copy(
            direccion = direccion
        )
    }
    private fun onCelularChanged(celular : String) {
        _uistate.value = _uistate.value.copy(
            celular = celular
        )
    }
    private fun onEmailChanged(email : String) {
        _uistate.value = _uistate.value.copy(
            email = email
        )
    }
    private fun onPasswordChanged(password : String) {
        _uistate.value = _uistate.value.copy(
            password = password
        )
    }

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.OnChangeApellidos -> onApellidosChanged(event.apellidos)
            is AuthEvent.OnChangeCedula -> onCedulaChanged(event.cedula)
            is AuthEvent.OnChangeCelular -> onCelularChanged(event.celular)
            is AuthEvent.OnChangeDireccion -> onDireccionChanged(event.direccion)
            is AuthEvent.OnChangeNombre -> onNombreChanged(event.nombre)
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

