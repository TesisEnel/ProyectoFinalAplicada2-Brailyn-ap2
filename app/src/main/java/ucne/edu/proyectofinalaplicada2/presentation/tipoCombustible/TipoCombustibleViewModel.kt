package ucne.edu.proyectofinalaplicada2.presentation.tipoCombustible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class TipoCombustibleViewModel @Inject constructor(
    private val tipoCombustibleRepository: TipoCombustibleRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(TipoCombustibleUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getProveedores()
    }

    private fun getProveedores(){
        viewModelScope.launch {
            tipoCombustibleRepository.getTiposCombustibles().collect { result ->
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
                                tipoCombustibles = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }


}
