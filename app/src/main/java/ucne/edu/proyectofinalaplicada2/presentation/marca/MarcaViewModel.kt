package ucne.edu.proyectofinalaplicada2.presentation.marca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.MarcaRepository
import ucne.edu.proyectofinalaplicada2.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MarcaViewModel @Inject constructor(
    private val marcaRepository: MarcaRepository

):ViewModel() {
    private val _uistate = MutableStateFlow(MarcaUiState())
    val uistate = _uistate.asStateFlow()


    init {
        getModelos()
    }

    private fun getModelos() {
        viewModelScope.launch {
            marcaRepository.getMarcas().collect { result ->

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
                                marcas = result.data ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }
    private fun onChangeMarcaId(marcaId: Int) {
        _uistate.update {
            it.copy(
                marcaId = marcaId
            )
        }
    }
    private fun onChangeNombreMarca(nombreMarca: String) {
        _uistate.update {
            it.copy(
                nombreMarca = nombreMarca
            )
        }
    }
    fun onEvent(event: MarcaEvent) {
        when (event) {
            is MarcaEvent.OnchangeMarcaId -> onChangeMarcaId(event.marcaId)
            is MarcaEvent.OnchangeNombreMarca -> onChangeNombreMarca(event.nombreMarca)
        }
    }

}