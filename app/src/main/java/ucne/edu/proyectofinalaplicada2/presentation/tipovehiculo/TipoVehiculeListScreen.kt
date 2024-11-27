package ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo

import androidx.compose.foundation.clickable
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
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaEvent
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaUiState
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaViewModel
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun TipoVehiculeListScreen(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    marcaViewModel: MarcaViewModel= hiltViewModel(),
    onBack: () -> Unit,
    onGoVehiculePresentation: (Int) -> Unit,
    marcaId: Int,
) {
    val uiState by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    val marcaUiState by marcaViewModel.uistate.collectAsStateWithLifecycle()
    TipoVehiculeBodyListScreen(
        vehiculoUistate = uiState,
        marcaUiState = marcaUiState,
        marcaId = marcaId,
        onBack = onBack,
        onGoVehiculePresentation =
            onGoVehiculePresentation
        ,
        onVehiculoEvent = { event -> vehiculoViewModel.onEvent(event) },
        onMarcaEvent = { event -> marcaViewModel.onEvent(event) }
    )

}

@Composable
fun TipoVehiculeBodyListScreen(
    vehiculoUistate: VehiculoUistate,
    marcaUiState: MarcaUiState,
    marcaId: Int,
    onBack: () -> Unit,
    onGoVehiculePresentation: (Int) -> Unit,
    onVehiculoEvent: (VehiculoEvent) -> Unit,
    onMarcaEvent: (MarcaEvent) -> Unit
) {
    LaunchedEffect(marcaId) {
        onMarcaEvent(MarcaEvent.OnchangeMarcaId(marcaId))
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
        if (vehiculoUistate.isLoading == true) {
            CircularProgressIndicator()
        } else {
            val newVehiculos = vehiculoUistate.vehiculos.filter { it.marcaId == marcaId }
            TipoVehiculeColumn(
                newVehiculos = newVehiculos,
                onGoVehiculePresentation = onGoVehiculePresentation,
                onEvent = onVehiculoEvent,
                onMarcaEvent = onMarcaEvent,
                marcaUiState = marcaUiState
            )
        }
    }

}


@Composable
fun TipoVehiculeColumn(
    newVehiculos: List<VehiculoEntity>,
    marcaUiState: MarcaUiState,
    onGoVehiculePresentation: (Int) -> Unit,
    onEvent: (VehiculoEvent) -> Unit,
    onMarcaEvent: (MarcaEvent) -> Unit
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
    ) {
        newVehiculos.forEach { vehiculoDto ->
            val image = vehiculoDto.imagePath.firstOrNull()
            val painter = rememberAsyncImagePainter(url + image)
            val marca = marcaUiState.marcas.find { it.marcaId == vehiculoDto.marcaId }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {

                        }
                    )
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TipoVehiculoList(
                    painter = painter,
                    marca = marca?.nombreMarca ?: "",
                    onGoVehiculePresentation = {onGoVehiculePresentation(vehiculoDto.vehiculoId ?: 0)},
                    vehiculoDto = vehiculoDto,
                    onMarcaEvent = onMarcaEvent
                )
            }

        }
    }
}