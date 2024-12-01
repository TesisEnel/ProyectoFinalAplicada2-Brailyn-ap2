package ucne.edu.proyectofinalaplicada2.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoConMarca
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun FiltraVehiculo(
    viewModel: VehiculoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()
    Column {
        SearchBar(
            searchQuery = uiState.searchQuery,
            onEvent = {event -> viewModel.onEvent(event)}
        )
        FiltraVehiculoBody(
            uiState = uiState,
        )
    }

}

@Composable
fun FiltraVehiculoBody(
    uiState: VehiculoUistate,
) {
    when {
        uiState.isLoading == true -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.filteredListIsEmpty-> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            )
            {
                Text(text = "No se encontraron vehiculos")
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(uiState.vehiculoConMarcas) { vehiculoConMarca ->
                    val painter = vehiculoConMarca.vehiculo.imagePath.firstOrNull()
                    MostrarVehiculos(
                        url = Constant.URL_BLOBSTORAGE + painter,
                        vehiculoConMarca = vehiculoConMarca,
                    )
                }
            }
        }
    }
}

@Composable
fun MostrarVehiculos(
    url: String,
    vehiculoConMarca: VehiculoConMarca,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(180.dp),
            // shape = CutCornerShape(20.dp)
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(3.dp,Color.Gray),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val painter = rememberAsyncImagePainter(url)
                Image(
                    painter = painter,
                    contentDescription = "null",
                    modifier = Modifier,
                    contentScale = ContentScale.Crop

                )
                Text(
                    text = vehiculoConMarca.nombreMarca?:"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Precio: ${vehiculoConMarca.vehiculo.precio}\n Año: ${vehiculoConMarca.vehiculo.anio}",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(6.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }

        }
    }
}



@Composable
fun SearchBar(
    searchQuery: String,
    onEvent: (VehiculoEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = searchQuery,
        onValueChange = { onEvent(VehiculoEvent.OnFilterVehiculos(it)) },
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
            .focusRequester(focusRequester)
    )
}
