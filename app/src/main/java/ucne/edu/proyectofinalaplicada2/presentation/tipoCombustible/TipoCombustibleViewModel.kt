package ucne.edu.proyectofinalaplicada2.presentation.tipoCombustible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.TipoCombustibleRepository
import javax.inject.Inject

@HiltViewModel
class TipoCombustibleViewModel @Inject constructor(
    private val tipoCombustibleRepository: TipoCombustibleRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(TipoCombustibleUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getTipoCombustible()
    }

    private fun getTipoCombustible(){
        viewModelScope.launch {
           val tiposCombustibles = tipoCombustibleRepository.getTiposCombustibles().data
            _uistate.update {
                it.copy(
                    tipoCombustibles = tiposCombustibles
                )
            }
        }
    }
}
