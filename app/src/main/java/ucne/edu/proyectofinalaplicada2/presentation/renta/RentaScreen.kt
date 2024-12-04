package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import ucne.edu.proyectofinalaplicada2.presentation.components.CardInfo
import ucne.edu.proyectofinalaplicada2.presentation.components.ConfirmRentaDialog
import ucne.edu.proyectofinalaplicada2.presentation.components.CustomDialog
import ucne.edu.proyectofinalaplicada2.presentation.components.DatePickerPopup
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoUistate
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun RentaScreen(
    rentaViewModel: RentaViewModel = hiltViewModel(),
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    vehiculoId: Int,
    rentaId: Int,
) {
    val rentaUiState by rentaViewModel.uistate.collectAsStateWithLifecycle()
    val vehiculoUiState by vehiculoViewModel.uistate.collectAsStateWithLifecycle()

    if (vehiculoUiState.isLoading == true) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    }else {
        RentaBodyScreen(
            rentaUiState = rentaUiState,
            vehiculoUiState = vehiculoUiState,
            vehiculoId = vehiculoId,
            onEvent = { rentaEvent -> rentaViewModel.onEvent(rentaEvent) },
            rentaId = rentaId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentaBodyScreen(
    rentaUiState: RentaUistate,
    vehiculoUiState: VehiculoUistate,
    vehiculoId: Int,
    rentaId: Int,
    onEvent: (RentaEvent) -> Unit = {},
) {
    val vehiculo = vehiculoUiState.vehiculos.find { it.vehiculoId == vehiculoId }
    var showDatePickerEntrega by remember { mutableStateOf(false) }
    val datePickerStateEntrega = rememberDatePickerState()
    val emailCliente = FirebaseAuth.getInstance().currentUser?.email

    LaunchedEffect( datePickerStateEntrega.selectedDateMillis) {
        val entregaDateMillis = datePickerStateEntrega.selectedDateMillis
        if (entregaDateMillis != null) {
            onEvent(RentaEvent.HandleDatePickerResult(entregaDateMillis, isStartDate = false))
            showDatePickerEntrega = false
        }
    }
    LaunchedEffect(vehiculoId) {
        if(rentaId>0){
            onEvent(RentaEvent.PrepareRentaData(emailCliente, vehiculoId,rentaId))
        }
        else{
            onEvent(RentaEvent.PrepareRentaData(emailCliente, vehiculoId,0))
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


                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(imagePaths.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(6.dp)
                                .background(
                                    color = if (pagerState.currentPage == index) Color.White else Color.Gray,
                                    shape = CircleShape
                                )
                        )
                    }
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
                                onValueChange = {onEvent(RentaEvent.OnchangeFechaRenta(it))},
                                label = { Text("Renta") },
                                trailingIcon = {
                                    IconButton(onClick = {  }) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )

                        }

                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = rentaUiState.fechaEntrega ?: "",
                                onValueChange = {
                                    onEvent(RentaEvent.OnchangeFechaEntrega(it))
                                },
                                label = { Text("Entrega") },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePickerEntrega = !showDatePickerEntrega }) {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )

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
                        enabled = rentaUiState.fechaEntrega?.isNotBlank() == true,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Text(text = if(rentaId> 0) "Actualizar" else "Rentar")
                    }
                    Card(
                        modifier = Modifier
                            .height(70.dp)
                    ) {
                        Text("")
                    }
                }
            }
        }
        if (rentaUiState.showModal) {
            ConfirmRentaDialog(
                rentaUiState = rentaUiState,
                vehiculo = vehiculo,
                onConfirm = {
                    if(rentaId > 0){
                        onEvent(RentaEvent.UpdateRenta)
                        onEvent(RentaEvent.CloseModal)

                    }else{
                        onEvent(RentaEvent.ConfirmRenta)
                        onEvent(RentaEvent.CloseModal)
                    }

                },
                onDismiss = { onEvent(RentaEvent.CloseModal) },
                rentaId = rentaId
            )
        }
        if(rentaUiState.success?.isNotEmpty() == true){
            CustomDialog(
                message = rentaUiState.success,
                isError = false,
                onDismiss = {
                    onEvent(RentaEvent.ClearSuccess)
                }
            )
        }
        if(rentaUiState.error?.isNotEmpty() == true){
            CustomDialog(
                message = rentaUiState.error,
                isError = false,
                onDismiss = {
                    onEvent(RentaEvent.ClearError)
                }
            )
        }
    }
}