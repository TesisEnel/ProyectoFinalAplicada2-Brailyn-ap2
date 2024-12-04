package ucne.edu.proyectofinalaplicada2.presentation.modelo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthViewModel
import ucne.edu.proyectofinalaplicada2.presentation.authentication.ClienteUiState
import ucne.edu.proyectofinalaplicada2.presentation.components.FiltroBotones
import ucne.edu.proyectofinalaplicada2.presentation.components.VehicleCard
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun TipoModeloListListScreen(
    modeloViewModel: ModeloViewModel = hiltViewModel(),
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onGoRenta: (Int) -> Unit,
    marcaId: Int,
    onGoEdit: (Int) -> Unit,
) {
    val uiState by modeloViewModel.uistate.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uistate.collectAsStateWithLifecycle()
    TipoModeloBodyListScreen(
        modeloUistate = uiState,
        marcaId = marcaId,
        onGoRenta = onGoRenta,
        onEvent = { event -> modeloViewModel.onEvent(event) },
        onGoEdit = onGoEdit,
        onVehiculoEvent = { event -> vehiculoViewModel.onEvent(event) },
        authUiState = authUiState
    )
}

@Composable
fun TipoModeloBodyListScreen(
    modeloUistate: ModeloUistate,
    marcaId: Int,
    onGoRenta: (Int) -> Unit,
    onEvent: (ModeloEvent) -> Unit = {},
    onGoEdit: (Int) -> Unit,
    onVehiculoEvent: (VehiculoEvent) -> Unit,
    authUiState: ClienteUiState
) {
    if (authUiState.isDataLoaded) {
        LaunchedEffect(authUiState.isAdmin) {

            if (!modeloUistate.isDataLoaded) {
                onEvent(ModeloEvent.GetVehiculosByMarcaId(marcaId, authUiState.isAdmin))
            }
        }
    }
    if (modeloUistate.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .padding(bottom = 5.dp, top = 20.dp)
        ) {
            Text(
                text = "Tipos de ${modeloUistate.marca?.nombreMarca ?: ""}",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp)
            )
            Text(
                text = "Aquí estarán todas los modelos de nuestras marcas",
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            if(authUiState.isAdmin){
                FiltroBotones(
                    onEvent = onEvent
                )
            }
            TipoModeloLazyColumn(
                modeloUistate = modeloUistate,
                onGoRenta = onGoRenta,
                onGoEdit = onGoEdit,
                onVehiculoEvent = onVehiculoEvent,
                authUistate = authUiState
            )
        }
    }

}


@Composable
fun TipoModeloLazyColumn(
    modeloUistate: ModeloUistate,
    onGoRenta: (Int) -> Unit,
    onGoEdit: (Int) -> Unit,
    authUistate: ClienteUiState,
    onVehiculoEvent: (VehiculoEvent) -> Unit
) {



    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(modeloUistate.listaFiltrada) { modelo ->
            VehicleCard(
                imageUrl = Constant.URL_BLOBSTORAGE + modelo.vehiculo.imagePath?.firstOrNull(),
                vehicleName = modelo.nombreModelo,
                vehicleDetails = "Precio: ${modelo.vehiculo.precio ?: "Sin detalles disponibles"}",
                vehiculoId = modelo.vehiculo.vehiculoId ?: 0,
                onGoRenta = onGoRenta,
                onGoEdit = onGoEdit,
                isAdmin = authUistate.isAdmin,
                onEvent = onVehiculoEvent,
                estado = if (modelo.vehiculo.estaRentado == true) "Rentado" else "Disponible",
                isRentado = modelo.vehiculo.estaRentado ?: false
            )
        }
    }
}




