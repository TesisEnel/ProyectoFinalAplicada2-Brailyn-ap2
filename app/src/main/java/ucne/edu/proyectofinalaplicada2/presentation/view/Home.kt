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
import androidx.compose.foundation.lazy.LazyColumn
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
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaEvent
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaUiState
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaViewModel
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun Home(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    marcaViewModel: MarcaViewModel = hiltViewModel(),
    onGoVehiculeList:(Int)-> Unit
) {
    val vehiculoUistate by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    val marcaUistate by marcaViewModel.uistate.collectAsStateWithLifecycle()
    if (vehiculoUistate.isLoading == true) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SearchBar()
            }
            item {
                VehiculosMasDestacados(
                    vehiculoUistate = vehiculoUistate,
                    marcaUiState = marcaUistate
                )
            }
            item {
                TiposDeVehiculos(
                    uiVehiculoState = vehiculoUistate,
                    marcaUiState = marcaUistate,
                    onGoVehiculeList = onGoVehiculeList,
                    onVehiculoEvent = { vehiculoEvent -> vehiculoViewModel.onEvent(vehiculoEvent) },
                    onMarcaEvent = { marcaEvent -> marcaViewModel.onEvent(marcaEvent) }
                )
            }
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
    vehiculoUistate: VehiculoUistate,
    marcaUiState: MarcaUiState
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
            items(vehiculoUistate.vehiculos) { vehiculo ->
                Box {
                    val marca =
                        marcaUiState.marcas.find { marcaDto -> marcaDto.marcaId == vehiculo.marcaId }
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
    uiVehiculoState: VehiculoUistate,
    marcaUiState: MarcaUiState,
    onGoVehiculeList:(Int)-> Unit,
    onVehiculoEvent: (VehiculoEvent) -> Unit,
    onMarcaEvent: (MarcaEvent) -> Unit

) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
    val shownMarcas = mutableSetOf<Int>()

    // Título de la Sección
    Text(
        text = "Tipos de marcas",
        fontFamily = FontFamily.Serif,
        fontSize = 20.sp,
        fontWeight = FontWeight.W700,
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)
    )

    // Contenido de la Sección
    uiVehiculoState.vehiculos.forEach { vehiculo ->
        val marca =
            marcaUiState.marcas.find { marcaDto -> marcaDto.marcaId == vehiculo.marcaId }

        if (marca != null && marca.marcaId !in shownMarcas) {
            shownMarcas.add(marca.marcaId)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val painter = rememberAsyncImagePainter(url + vehiculo.imagePath.firstOrNull())
                TipoVehiculoList(
                    painter = painter,
                    marca = marca.nombreMarca,
                    onGoVehiculeList = onGoVehiculeList,
                    vehiculoDto = vehiculo,
                    onVehiculoEvent = onVehiculoEvent,
                    onMarcaEvent = onMarcaEvent
                )
            }
        }
    }
}



