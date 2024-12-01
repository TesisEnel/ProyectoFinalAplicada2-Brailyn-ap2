package ucne.edu.proyectofinalaplicada2.presentation.modelo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.components.VehicleCard
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun TipoModeloListListScreen(
    modeloViewModel: ModeloViewModel = hiltViewModel(),
    onGoVehiculePresentation: (Int) -> Unit,
    marcaId: Int,
) {
    val uiState by modeloViewModel.uistate.collectAsStateWithLifecycle()
    TipoModeloBodyListScreen(
        modeloUistate = uiState,
        marcaId = marcaId,
        onGoRenta = onGoVehiculePresentation,
        onEvent = { event -> modeloViewModel.onEvent(event) }
    )
}

@Composable
fun TipoModeloBodyListScreen(
    modeloUistate: ModeloUistate,
    marcaId: Int,
    onGoRenta: (Int) -> Unit,
    onEvent: (ModeloEvent) -> Unit = {},
) {
    if (modeloUistate.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp, top = 20.dp)
        ) {
            Text(
                text = "Tipos de ${modeloUistate.marca?.nombreMarca?:""}",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp)
            )
            Text(
                text = "Aquí estarán todas las marcas de nuestros vehículos",
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            TipoModeloLazyColumn(
                modeloUistate = modeloUistate,
                onGoRenta = onGoRenta,
                marcaId = marcaId,
                onEvent = onEvent
            )
        }
    }

}


@Composable
fun TipoModeloLazyColumn(
    modeloUistate: ModeloUistate,
    onGoRenta: (Int) -> Unit,
    marcaId: Int,
    onEvent: (ModeloEvent) -> Unit = {},
) {
    if (!modeloUistate.isDataLoaded) {
        onEvent(ModeloEvent.GetVehiculosByMarcaId(marcaId))
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(modeloUistate.modeloConVehiculos) { modelo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGoRenta(modelo.vehiculo.vehiculoId ?: 0) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VehicleCard(
                    imageUrl = Constant.URL_BLOBSTORAGE + modelo.vehiculo.imagePath.firstOrNull(),
                    vehicleName = modelo.nombreModelo,
                    vehicleDetails = "Precio: ${modelo.vehiculo.precio ?: "Sin detalles disponibles"}",
                    modifier = Modifier.weight(1f),
                    vehiculoId = modelo.vehiculo.vehiculoId ?: 0,
                    onGoRenta = onGoRenta
                )
            }
        }
    }
}



