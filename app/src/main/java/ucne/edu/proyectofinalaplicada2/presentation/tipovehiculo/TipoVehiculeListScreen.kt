package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.data.remote.dto.ModeloDto

import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.Uistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun TipoVehiculeListScreen(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    vehiculoId: Int
) {
    val uiState by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    TipoVehiculeBodyListScreen(
        uiState = uiState,
        marcaId = vehiculoId,
        onBack = onBack,
        onCreateRenta = onCreateRenta,
        onEvent = { event -> vehiculoViewModel.onEvent(event) }
    )

}

@Composable
fun TipoVehiculeBodyListScreen(
    uiState: Uistate,
    marcaId: Int,
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    onEvent: (VehiculoEvent) -> Unit = {}
) {
    val url = "https://rentcar.azurewebsites.net/images/"
    val vehiculo = uiState.vehiculos.find { it.vehiculoId == marcaId }
    Column(
        modifier = Modifier
            .padding(bottom = 5.dp, top = 20.dp)
    ) {
        Text(
            text = "Tipos de vehiculos",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)
        )
        TipoVehiculeColumn(
            modelos = uiState.modelos
        )
    }

}


@Composable
fun TipoVehiculeColumn(
    modelos: List<ModeloDto>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
    ) {


        modelos.forEach{
            Text("Holaaa ${it.modeloVehiculo}")
        }
    }
}