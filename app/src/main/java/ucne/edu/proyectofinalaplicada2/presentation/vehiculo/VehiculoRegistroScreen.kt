package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.components.InputSelect

@Composable
fun VehiculoRegistroScreen(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    onBackHome: ()-> Unit
) {
    val uistate by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    VehiculoBodyRegistroScreen(
        uistate =uistate,
        onBackHome = onBackHome,
        onEnvent = {vehiculoEvent -> vehiculoViewModel.onEvent(vehiculoEvent) }
    )
}


@Composable
fun VehiculoBodyRegistroScreen(
    uistate: Uistate,
    onBackHome: () -> Unit,
    onEnvent:(VehiculoEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InputSelect(
            label = "Marca",
            options =uistate.marcas,
            onOptionSelected = {onEnvent(VehiculoEvent.OnChangeMarcaId(it.marcaId))},
            labelSelector = {it.nombreMarca}
        )
    }
}

