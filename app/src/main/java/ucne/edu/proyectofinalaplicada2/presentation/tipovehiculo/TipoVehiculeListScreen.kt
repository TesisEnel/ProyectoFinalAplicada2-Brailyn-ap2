package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.components.TipoVehiculoList
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
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
    LaunchedEffect(marcaId) {
        onEvent(VehiculoEvent.OnChangeMarcaId(marcaId))
    }
    Column(
        modifier = Modifier
            .padding(bottom = 5.dp, top = 20.dp)
    ) {
        Text(
            text = "Tipos de modelos",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)
        )
        if (uiState.isLoading == true) {
            CircularProgressIndicator()
        } else {
            val newVehiculos = uiState.vehiculos.filter { it.marcaId == marcaId }
            TipoVehiculeColumn(
                newVehiculos = newVehiculos,
                uiState = uiState,
            )
        }
    }

}


@Composable
fun TipoVehiculeColumn(
    newVehiculos: List<VehiculoDto>,
    uiState: Uistate,
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
    ) {
        newVehiculos.forEach{ vehiculoDto ->
            val image = vehiculoDto.imagePath.firstOrNull()
            val painter = rememberAsyncImagePainter(url + image)
            val marca = uiState.marcas.find { it.marcaId == vehiculoDto.marcaId }
            TipoVehiculoList(
                painter = painter,
                marca = marca?.nombreMarca ?:  "",
                onGoVehiculePresentation = {},
                vehiculoDto = vehiculoDto,
                onEvent = {}
            )
        }
    }
}