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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.Converter
import ucne.edu.proyectofinalaplicada2.data.remote.dto.RentaDto
import ucne.edu.proyectofinalaplicada2.presentation.cliente.ClienteUistate
import ucne.edu.proyectofinalaplicada2.presentation.cliente.ClienteViewModel
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaUiState
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaViewModel
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun RentaScreen(
    rentaViewModel: RentaViewModel = hiltViewModel(),
    vehicoViewModel: VehiculoViewModel = hiltViewModel(),
    marcaViewModel: MarcaViewModel = hiltViewModel(),
    clienteViewModel: ClienteViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    vehiculoId: Int,
) {
    val rentaUiState by rentaViewModel.uistate.collectAsStateWithLifecycle()
    val vehiculoUiState by vehicoViewModel.uistate.collectAsStateWithLifecycle()
    val marcaUiState by marcaViewModel.uistate.collectAsStateWithLifecycle()
    val clienteUiState by clienteViewModel.uistate.collectAsStateWithLifecycle()

    RentaBodyScreen(
        rentaUiState = rentaUiState,
        vehiculoUiState = vehiculoUiState,
        marcaUiState = marcaUiState,
        clienteUiState = clienteUiState,
        onBack = onBack,
        onCreateRenta = onCreateRenta,
        vehiculoId = vehiculoId,
        onEvent = { rentaEvent -> rentaViewModel.onEvent(rentaEvent) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentaBodyScreen(
    rentaUiState: RentaUistate,
    vehiculoUiState: VehiculoUistate,
    marcaUiState: MarcaUiState,
    clienteUiState: ClienteUistate,
    onBack: () -> Unit,
    onCreateRenta: () -> Unit,
    vehiculoId: Int,
    onEvent: (RentaEvent) -> Unit = {}
) {
    val url = "https://rentcarblobstorage.blob.core.windows.net/images/"
    val vehiculo = vehiculoUiState.vehiculos.find { it.vehiculoId == vehiculoId }
    var showDatePickerEntrega by remember { mutableStateOf(false) }
    val datePickerStateEntrega = rememberDatePickerState()
    var showModal by remember { mutableStateOf(false) }
    var showDatePickerRenta by remember { mutableStateOf(false) }
    val datePickerStateRenta = rememberDatePickerState()


    LaunchedEffect(datePickerStateRenta.selectedDateMillis) {
        datePickerStateRenta.selectedDateMillis?.let { selectedDateMillis ->
            val selectedDate = Converter().convertToDate(selectedDateMillis)
            onEvent(RentaEvent.OnchangeFechaRenta(selectedDate))
            delay(300)
            showDatePickerRenta = false
        }
    }

    // Manejar fecha de entrega
    LaunchedEffect(datePickerStateEntrega.selectedDateMillis) {
        datePickerStateEntrega.selectedDateMillis?.let { selectedDateMillis ->
            val selectedDate = Converter().convertToDate(selectedDateMillis)
            onEvent(RentaEvent.OnchangeFechaEntrega(selectedDate))
            delay(300)
            showDatePickerEntrega = false
        }
    }

    Column{
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
        Text(
            text = "Selecciona la fecha para rentar",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.W700
        )
        // Row con dos DatePickers
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
        ) {
            Row {
                OutlinedTextField(
                    value = rentaUiState.fechaRenta,
                    onValueChange = {},
                    label = { Text("Renta") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePickerRenta = !showDatePickerRenta }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .padding(end = 15.dp)
                    )

                if (showDatePickerRenta) {
                    Popup(
                        onDismissRequest = { showDatePickerRenta = false },
                        alignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 10.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            DatePicker(
                                state = datePickerStateRenta,
                                showModeToggle = false,
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = rentaUiState.fechaEntrega?:"",
                    onValueChange = {},
                    label = { Text("Entrega") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePickerEntrega = !showDatePickerEntrega }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)

                )
                if (showDatePickerEntrega) {
                    Popup(
                        onDismissRequest = { showDatePickerEntrega = false },
                        alignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 10.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            DatePicker(
                                state = datePickerStateEntrega,
                                showModeToggle = false
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onEvent(RentaEvent.CalculeTotal(rentaUiState.fechaRenta,rentaUiState.fechaEntrega?:"",vehiculo?.precio?:0))
                showModal = true
              },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Rentar Ahora")
        }
        if (showModal) {
            val marca = marcaUiState.marcas.find { it.marcaId == vehiculo?.marcaId }
            val emailCliente = FirebaseAuth.getInstance().currentUser?.email
            val cliente = clienteUiState.clientes.find { it.email == emailCliente }
            val renta = RentaDto(
                clienteId = cliente?.clienteId,
                vehiculoId = vehiculo?.vehiculoId,
                fechaRenta = rentaUiState.fechaRenta,
                fechaEntrega = rentaUiState.fechaEntrega,
                total = rentaUiState.total
            )
            ConfirmRentaDialog(
                vehiculoName = marca?.nombreMarca,
                fechaRenta = rentaUiState.fechaRenta,
                fechaEntrega = rentaUiState.fechaEntrega,
                costoTotal = rentaUiState.total?.toDouble(),
                onConfirm = {
                    showModal = false
                },
                onDismiss = {
                    showModal = false

                },

                createRenta = { onEvent(RentaEvent.Save(renta)) },
            )
            Text(rentaUiState.error?:"", color = MaterialTheme.colorScheme.error)
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

@Composable
fun ConfirmRentaDialog(
    vehiculoName: String?,
    fechaRenta: String?,
    fechaEntrega: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    costoTotal: Double?,
    createRenta: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Confirmar Renta")
        },
        text = {
            Column {
                Text("Vehículo: ${vehiculoName ?: "No disponible"}")
                Text("Fecha de Salida: ${fechaRenta ?: "No seleccionada"}")
                Text("Fecha de Entrada: ${fechaEntrega ?: "No seleccionada"}")
                Text("El costo total es: ${costoTotal ?: ""}")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm()
                    createRenta()
                }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}