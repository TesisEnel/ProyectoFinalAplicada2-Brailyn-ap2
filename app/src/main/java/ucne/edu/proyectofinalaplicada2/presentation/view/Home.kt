package ucne.edu.proyectofinalaplicada2.presentation.view


import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthViewModel
import ucne.edu.proyectofinalaplicada2.presentation.authentication.ClienteUiState
import ucne.edu.proyectofinalaplicada2.presentation.components.ImageCard
import ucne.edu.proyectofinalaplicada2.presentation.components.TipoVehiculoList
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun Home(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onGoVehiculeList:(Int)-> Unit,
    onGoSearch: () -> Unit,
    onGoRenta: (Int) -> Unit = {}
) {
    val vehiculoUistate by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    val authUistate by authViewModel.uistate.collectAsStateWithLifecycle()
    if (vehiculoUistate.isLoading == true || vehiculoUistate.isLoadingData == true) {
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
                FakeSearchBar(
                    onGoSearch = onGoSearch
                )
            }
            item {
                ListaDeVehiculos(
                    vehiculoUistate = vehiculoUistate,
                    onGoRenta = onGoRenta,
                    authUistate = authUistate,
                    onEvent = { vehiculoViewModel.onEvent(it) }
                )
            }
            item {
                TiposDeVehiculos(
                    vehiculoUiState = vehiculoUistate,
                    onGoVehiculeList = onGoVehiculeList,
                )
            }
        }
    }
}

@Composable
fun FakeSearchBar(
    onGoSearch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 80.dp)
            .padding(15.dp)
            .clickable(onClick = onGoSearch),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.placeholder_search),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.placeholder_search),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@Composable
fun ListaDeVehiculos(
    vehiculoUistate: VehiculoUistate,
    onGoRenta: (Int) -> Unit = {},
    authUistate: ClienteUiState,
    onEvent: (VehiculoEvent) -> Unit = {},
) {
    if (authUistate.isDataLoaded) {
        LaunchedEffect(authUistate.isAdmin) {
            onEvent(VehiculoEvent.GetVehiculosFiltered(vehiculoUistate.vehiculos, authUistate.isAdmin))
        }
    }
    Column(
        modifier = Modifier.padding(bottom = 5.dp, top = 6.dp),
    ) {
        Text(
            text = "Vehículos",
            fontFamily = FontFamily.Serif,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)
        )
        Text(
            text = "Aquí encontrarás todos nuestros vehiculos",
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            items(vehiculoUistate.vehiculoConMarcas) { vehiculoConMarca ->
                Box(
                    modifier = Modifier
                        .heightIn(max = 150.dp)
                ) {
                    ImageCard(
                        painter = rememberAsyncImagePainter(Constant.URL_BLOBSTORAGE + vehiculoConMarca.vehiculo.imagePath?.firstOrNull()),
                        contentDescription = "",
                        title = vehiculoConMarca.nombreModelo?:"",
                        vehiculoId = vehiculoConMarca.vehiculo.vehiculoId?:0,
                        onGoRenta = onGoRenta
                    )
                }
            }
        }
    }
}

@Composable
fun TiposDeVehiculos(
    vehiculoUiState: VehiculoUistate,
    onGoVehiculeList: (Int) -> Unit,
) {
    val marcasUnicas = mutableListOf<Int>()
    Column {
        Text(
            text = "Tipos de marcas",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)
        )
        Text(
            text = "Aquí estarán todas las marcas de nuestros vehículos",
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        vehiculoUiState.vehiculoConMarcas.forEach { vehiculoConMarca ->
            val marcaId = vehiculoConMarca.vehiculo.marcaId ?: 0
            if (!marcasUnicas.contains(marcaId)) {
                marcasUnicas.add(marcaId)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val painter = rememberAsyncImagePainter(
                        Constant.URL_BLOBSTORAGE + vehiculoConMarca.vehiculo.imagePath?.firstOrNull()
                    )

                    TipoVehiculoList(
                        painter = painter,
                        marca = vehiculoConMarca.nombreMarca ?: "",
                        onGoVehiculeList = { onGoVehiculeList(marcaId) },
                        vehiculoConMarca = vehiculoConMarca,
                        onMarcaEvent = {}
                    )
                }
            }
        }
    }
}





