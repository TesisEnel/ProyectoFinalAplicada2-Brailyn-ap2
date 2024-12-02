package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirmar eliminación")
        },
        text = {
            Text(text = "¿Estás seguro de que deseas eliminar este vehículo? Esta acción no se puede deshacer.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}