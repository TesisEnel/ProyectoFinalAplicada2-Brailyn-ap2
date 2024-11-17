package ucne.edu.proyectofinalaplicada2.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.components.ImageCard
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.Uistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel


@Composable
fun VehiculePresentation(
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    viewModel: VehiculoViewModel = hiltViewModel(),
    vehiculoId: Int
) {
    val uiState = viewModel.uistate.collectAsStateWithLifecycle()
    VehiculeBodyPresentation(
        uiState = uiState.value,
        onBack = onBack,
        onCreateRenta = onCreateRenta,
        vehiculoId = vehiculoId
    )
}

@Composable
fun VehiculeBodyPresentation(
    uiState: Uistate,
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    vehiculoId: Int
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
    val vehiculo = uiState.vehiculos.find { it.vehiculoId == vehiculoId }
    Column {
        vehiculo?.imagePath?.forEach{ nombre ->
            val painter = rememberAsyncImagePainter(url + nombre)
            ImageCard(
                contentDescription = "",
                painter = painter,
                title = vehiculo.descripcion ?: "",
                height = 180,
                width = 150
            )
        }
    }

}