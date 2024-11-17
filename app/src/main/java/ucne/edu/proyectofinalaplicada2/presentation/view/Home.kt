package ucne.edu.proyectofinalaplicada2.presentation.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.data.remote.dto.VehiculoDto
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.Uistate
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
                onGoVehiculePresentation
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
                        painter = rememberAsyncImagePainter(url + vehiculo.imagePath),
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
    onGoVehiculePresentation:(Int)-> Unit
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
                    val painter = rememberAsyncImagePainter(url + vehiculo.imagePath)
                    val marca =
                        uiState.marcas.find { marcaDto -> marcaDto.marcaId == vehiculo.marcaId }
                    TipoVehiculoList(
                        painter = painter,
                        marca = marca?.nombreMarca ?: "",
                        listaModeloEjemplo = "Camry, Highlander, Hilux",
                        onGoVehiculePresentation = onGoVehiculePresentation,
                        vehiculoDto = vehiculo
                    )
                }
            }
        }
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    height: Int,
    width: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(height.dp)
            .width(width.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Box {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 170f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(text = title, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}


@Composable
fun TipoVehiculoList(
    painter: Painter,
    marca: String,
    listaModeloEjemplo: String,
    onGoVehiculePresentation:(Int)-> Unit,
    vehiculoDto: VehiculoDto
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onGoVehiculePresentation(vehiculoDto.vehiculoId?:0) })
        ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {
        Row {
            VehiculeImage(
                painter = painter
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = marca, style = MaterialTheme.typography.headlineSmall)
                Text(text = listaModeloEjemplo, style = MaterialTheme.typography.labelLarge)
            }
        }
    }

}

@Composable
fun VehiculeImage(
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}
