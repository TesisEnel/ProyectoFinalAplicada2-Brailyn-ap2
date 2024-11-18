package ucne.edu.proyectofinalaplicada2.presentation.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.components.ImageCard
import ucne.edu.proyectofinalaplicada2.components.TipoVehiculoList
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.Uistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun Home(
    viewModel: VehiculoViewModel = hiltViewModel(),
    onGoVehiculePresentation:(Int)-> Unit
) {
    val uistate by viewModel.uistate.collectAsStateWithLifecycle()
    if (uistate.isLoading == true) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar()
            VehiculosMasDestacados(
                uiState = uistate
            )
            TiposDeVehiculos(
                uiState = uistate,
                onGoVehiculePresentation,
                onEvent = { vehiculoEvent -> viewModel.onEvent(vehiculoEvent) }
            )
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
            focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .padding(15.dp)
    )
}

@Composable
fun VehiculosMasDestacados(
    uiState: Uistate
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
    Column(
        modifier = Modifier.padding(bottom = 5.dp, top = 20.dp)
    ) {
        Text(
            text = "Vehiculos destacados",
            fontFamily = FontFamily.Serif,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.padding(vertical = 5.dp),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            items(uiState.vehiculos) { vehiculo ->
                Box {
                    val marca =
                        uiState.marcas.find { marcaDto -> marcaDto.marcaId == vehiculo.marcaId }
                    ImageCard(
                        painter = rememberAsyncImagePainter(url + vehiculo.imagePath.firstOrNull()),
                        contentDescription = vehiculo.descripcion ?: "",
                        title = marca?.nombreMarca?:"",
                        height = 180,
                        width = 150
                    )
                }
            }
        }
    }
}

@Composable
fun TiposDeVehiculos(
    uiState: Uistate,
    onGoVehiculePresentation:(Int)-> Unit,
    onEvent: (VehiculoEvent) -> Unit
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
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
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(),
        ) {
            uiState.vehiculos.forEach { vehiculo ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val painter = rememberAsyncImagePainter(url + vehiculo.imagePath.firstOrNull())
                    val marca =
                        uiState.marcas.find { marcaDto -> marcaDto.marcaId == vehiculo.marcaId }
                    TipoVehiculoList(
                        painter = painter,
                        marca = marca?.nombreMarca ?: "",
                        listaModeloEjemplo = "Camry, Highlander, Hilux",
                        onGoVehiculePresentation = onGoVehiculePresentation,
                        vehiculoDto = vehiculo,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}



