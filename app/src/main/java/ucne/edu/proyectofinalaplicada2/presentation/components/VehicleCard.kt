package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.CustomDialog
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent

@Composable
fun VehicleCard(
    imageUrl: String,
    vehicleName: String,
    vehicleDetails: String,
    vehiculoId: Int,
    onGoRenta: (Int) -> Unit = {},
    onGoEdit: (Int) -> Unit = {},
    onEvent: (VehiculoEvent) -> Unit = {},
    isAdmin: Boolean,
    estado: String = "",
    isRentado: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp)
                .clickable(onClick = { onGoRenta(vehiculoId) }),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = vehicleName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                        Box {
                            if (isAdmin) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Más opciones",
                                    modifier = Modifier.clickable { expanded = true }
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        onGoEdit(vehiculoId)
                                    },
                                    text = { Text("Editar") }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        showDeleteDialog = true
                                    },
                                    text = { Text("Eliminar") }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = vehicleDetails)
                    Text(text = "Estado: $estado")
                }
            }
        }

        if (showDeleteDialog) {
            ConfirmDeleteDialog(
                onConfirm = {
                    if (!isRentado) {
                        onEvent(VehiculoEvent.DeleteVehiculo(vehiculoId))
                        showDeleteDialog = false
                    } else {
                        showErrorDialog = true
                    }

                },
                onDismiss = { showDeleteDialog = false }
            )
        }

      if (showErrorDialog) {
          CustomDialog(
              onDismiss = { showErrorDialog = false },
              message = "El vehículo está rentado",
              isError = true
          )
      }
    }
}

