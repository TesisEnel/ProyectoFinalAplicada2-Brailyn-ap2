package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.repository.TipoVehiculoRepository
import javax.inject.Inject

@HiltViewModel
class TipoVehiculoViewModel @Inject constructor(
    private val tipoVehiculoRepository: TipoVehiculoRepository
) : ViewModel() {
    private val _uistate = MutableStateFlow(TipoVehiculoUistate())
    val uistate = _uistate.asStateFlow()

    init {
        getTipoVehiculos()
    }

    private fun getTipoVehiculos(){
        viewModelScope.launch {
            val tipoVehiculos = tipoVehiculoRepository.getTiposVehiculos().data
            _uistate.update {
                it.copy(tipoVehiculos = tipoVehiculos?: emptyList())
            }
        }
    }
}
