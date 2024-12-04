package ucne.edu.proyectofinalaplicada2.presentation.proveedor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthEvent
import ucne.edu.proyectofinalaplicada2.presentation.authentication.ErrorText
import ucne.edu.proyectofinalaplicada2.presentation.authentication.PhoneInputField
import ucne.edu.proyectofinalaplicada2.presentation.authentication.PhoneInputField2
import ucne.edu.proyectofinalaplicada2.presentation.components.CustomDialog
import ucne.edu.proyectofinalaplicada2.utils.Constant


@Composable
fun ProveedorRegistroScreen(
    viewModel: ProveedorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()
    ProveedorRegistroBodyScreen(
        uiState = uiState,
        onEvent = { event -> viewModel.onEvent(event) },
    )
}

@Composable
fun ProveedorRegistroBodyScreen(
    uiState: ProveedorUistate,
    onEvent: (ProveedorEvent) -> Unit,
) {

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 30.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 25.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    AsyncImage(
                        model = Constant.URL_BLOBSTORAGE + "poroveedores.jpg",
                        contentDescription = "Imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { onEvent(ProveedorEvent.OnChangeNombre(it)) },
                label = { Text(text = "Nombre") },
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp),
            )
            ErrorText(uiState.errorNombre)

            Spacer(modifier = Modifier.height(8.dp))

            PhoneInputField2(
                phone = uiState.telefono,
                onPhoneChange = { onEvent(ProveedorEvent.OnChangeTelefono(it)) },
            )
            ErrorText(uiState.errorTelefono)

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onEvent(ProveedorEvent.OnSave) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(text = "Registrar Proveedor")
            }
            if (uiState.success.isNotEmpty()) {
                CustomDialog(
                    message = uiState.success,
                    isError = uiState.success.isBlank(),
                    onDismiss = {
                        onEvent(ProveedorEvent.OnClearSuccess)
                    }
                )
            }
        }
    }

}