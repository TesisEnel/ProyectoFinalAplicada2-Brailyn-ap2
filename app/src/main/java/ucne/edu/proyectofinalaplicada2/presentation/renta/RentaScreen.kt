package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun VehiculePresentation(
    viewModel: VehiculoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    vehiculoId: Int,
    onEvent: (VehiculoEvent) -> Unit = {}
) {
    LaunchedEffect(vehiculoId) {
        onEvent(VehiculoEvent.GetVehiculos)
    }
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()

    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
    val vehiculo = uiState.vehiculos.find { it.vehiculoId == vehiculoId }

    Column {
        // Carousel Section
        vehiculo?.imagePath?.let { imagePaths ->
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    HorizontalPager(
                        count = imagePaths.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val painter = rememberAsyncImagePainter(url + imagePaths[page])
                        ImageCard(
                            contentDescription = "Vehículo Imagen $page",
                            painter = painter,
                        )
                    }

                    // Buttons for Navigation
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val newPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                                    pagerState.scrollToPage(newPage)
                                }
                            },
                            enabled = pagerState.currentPage > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Página Anterior"
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(imagePaths.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .size(if (isSelected) 14.dp else 8.dp) // Tamaño más prominente
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            shape = MaterialTheme.shapes.small
                                        )
                                )
                            }
                        }

                        // Right Button
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val newPage =
                                        (pagerState.currentPage + 1).coerceAtMost(imagePaths.size - 1)
                                    pagerState.scrollToPage(newPage)
                                }
                            },
                            enabled = pagerState.currentPage < imagePaths.size - 1
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Página Siguiente"
                            )
                        }



                    }

                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rent Now Section
        Button(
            onClick = onCreateRenta,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Rentar Ahora")
        }
    }
}

@Composable
fun ImageCard(
    contentDescription: String,
    painter: Painter,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),

        ) {
        Box {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
