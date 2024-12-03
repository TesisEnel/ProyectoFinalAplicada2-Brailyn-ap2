package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import ucne.edu.proyectofinalaplicada2.Converter
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun RentaScreen(
    rentaViewModel: RentaViewModel = hiltViewModel(),
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    vehiculoId: Int,
) {
    val rentaUiState by rentaViewModel.uistate.collectAsStateWithLifecycle()
    val vehiculoUiState by vehiculoViewModel.uistate.collectAsStateWithLifecycle()
    val cliente = FirebaseAuth.getInstance().currentUser?.email
    LaunchedEffect(Unit) {
        rentaViewModel.onEvent(RentaEvent.PrepareRentaData(cliente,vehiculoId))
    }

    RentaBodyScreen(
        rentaUiState = rentaUiState,
        vehiculoUiState = vehiculoUiState,
        vehiculoId = vehiculoId,
        onEvent = { rentaEvent -> rentaViewModel.onEvent(rentaEvent) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentaBodyScreen(
    rentaUiState: RentaUistate,
    vehiculoUiState: VehiculoUistate,
    vehiculoId: Int,
    onEvent: (RentaEvent) -> Unit = {}
) {
    val vehiculo = vehiculoUiState.vehiculos.find { it.vehiculoId == vehiculoId }
    var showDatePickerEntrega by remember { mutableStateOf(false) }
    val datePickerStateEntrega = rememberDatePickerState()
    var showDatePickerRenta by remember { mutableStateOf(false) }
    val datePickerStateRenta = rememberDatePickerState()


    LaunchedEffect(Unit) {
        val emailCliente = FirebaseAuth.getInstance().currentUser?.email
        onEvent(RentaEvent.PrepareRentaData(emailCliente, vehiculoId))
    }


    LaunchedEffect(datePickerStateRenta.selectedDateMillis) {
        handleDatePickerResult(
            datePickerStateRenta.selectedDateMillis
        ) { selectedDate ->
            onEvent(RentaEvent.OnchangeFechaRenta(selectedDate))
            showDatePickerRenta = false
        }
    }

    LaunchedEffect(datePickerStateEntrega.selectedDateMillis) {
        handleDatePickerResult(
            datePickerStateEntrega.selectedDateMillis
        ) { selectedDate ->
            onEvent(RentaEvent.OnchangeFechaEntrega(selectedDate))
            showDatePickerEntrega = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        vehiculo?.imagePath?.let { imagePaths ->
            val pagerState = rememberPagerState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    count = imagePaths.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val painter =
                        rememberAsyncImagePainter(Constant.URL_BLOBSTORAGE + imagePaths[page])
                    Image(
                        painter = painter,
                        contentDescription = "Vehículo Imagen $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 220.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(510.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${rentaUiState.marca?.nombreMarca} ${rentaUiState.modelo?.modeloVehiculo}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Información del vehículo",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = vehiculo?.descripcion ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Normal
                    )

                    Text(
                        text = "Especificaciones Técnicas",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CardInfo("Precio/Día", "${vehiculo?.precio ?: "N/A"} DOP")
                        CardInfo("Tipo de Combustible", rentaUiState.vehiculoConCombustible ?: "N/A")
                        CardInfo("Año", vehiculo?.anio.toString())
                        CardInfo("Tipo de Vehículo", rentaUiState.vehiculoConTipo ?: "N/A")
                    }

                    Text(
                        text = "Selecciona la fecha para rentar",
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = rentaUiState.fechaRenta,
                                onValueChange = {},
                                label = { Text("Renta") },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePickerRenta = !showDatePickerRenta }) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            rentaUiState.errorFechaRenta?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                )
                            }
                            rentaUiState.error?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                )
                            }

                        }

                        if (showDatePickerRenta) {
                            DatePickerPopup(
                                onDismissRequest = { showDatePickerRenta = false },
                                state = datePickerStateRenta
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = rentaUiState.fechaEntrega ?: "",
                                onValueChange = { onEvent(RentaEvent.OnchangeFechaEntrega(it)) },
                                label = { Text("Entrega") },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePickerEntrega = !showDatePickerEntrega }) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            rentaUiState.errorFechaEntrega?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                )
                            }
                        }

                        if (showDatePickerEntrega) {
                            DatePickerPopup(
                                onDismissRequest = { showDatePickerEntrega = false },
                                state = datePickerStateEntrega
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onEvent(
                                RentaEvent.CalculeTotal(
                                    fechaRenta = rentaUiState.fechaRenta,
                                    fechaEntrega = rentaUiState.fechaEntrega ?: "",
                                    costoDiario = vehiculo?.precio ?: 0
                                )
                            )
                        },
                        enabled = rentaUiState.errorFechaRenta.isNullOrEmpty() &&
                                rentaUiState.errorFechaEntrega.isNullOrEmpty(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Rentar Ahora")
                    }
                }
            }
        }
        if (rentaUiState.showModal) {
            ConfirmRentaDialog(
                rentaUiState = rentaUiState,
                vehiculo = vehiculo,
                onConfirm = {
                    onEvent(RentaEvent.ConfirmRenta)
                    onEvent(RentaEvent.CloseModal)
                },
                onDismiss = { onEvent(RentaEvent.CloseModal) },
            )
        }
    }
}

@Composable
fun CardInfo(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPopup(onDismissRequest: () -> Unit, state: DatePickerState) {
    Popup(
        onDismissRequest = onDismissRequest,
        alignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .clickable(onClick = onDismissRequest),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(380.dp)
                    .height(500.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DatePicker(
                        state = state,
                        showModeToggle = false,
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmRentaDialog(
    rentaUiState: RentaUistate,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    vehiculo: VehiculoEntity?,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Confirmar Renta")
        },
        text = {
            Card(
                modifier = Modifier.padding(20.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    InfoRow(label = "Vehículo:", value = rentaUiState.vehiculoNombre ?: "N/A")
                    InfoRow(label = "Modelo:", value = rentaUiState.vehiculoModelo ?: "N/A")
                    InfoRow(label = "Año:", value = vehiculo?.anio?.toString() ?: "N/A")
                    InfoRow(label = "Fecha de Salida:", value = rentaUiState.fechaRenta)
                    InfoRow(label = "Fecha de Entrada:", value = rentaUiState.fechaEntrega ?: "N/A")
                    InfoRow(label = "Precio/Día:", value = "${vehiculo?.precio ?: "N/A"} DOP")
                    InfoRow(label = "Días Totales:", value = rentaUiState.cantidadDias?.toString() ?: "N/A")

                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Costo Total:",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${rentaUiState.total ?: "N/A"} DOP",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Equivalente a:",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${rentaUiState.total?.div(60) ?: "N/A"} USD",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
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

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
fun handleDatePickerResult(
    selectedDateMillis: Long?,
    onDateSelected: (String) -> Unit,
) {
    selectedDateMillis?.let { millis ->
        val selectedDate = Converter().convertToDate(millis)
        onDateSelected(selectedDate)
    }
}