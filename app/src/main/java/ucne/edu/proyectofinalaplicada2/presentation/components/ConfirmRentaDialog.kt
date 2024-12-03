package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ucne.edu.proyectofinalaplicada2.data.local.entities.VehiculoEntity
import ucne.edu.proyectofinalaplicada2.presentation.renta.RentaUistate

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
