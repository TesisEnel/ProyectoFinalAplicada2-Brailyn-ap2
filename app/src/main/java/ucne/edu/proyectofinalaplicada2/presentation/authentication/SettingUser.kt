package ucne.edu.proyectofinalaplicada2.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun SettingUser(
    goToBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()
    EditarUsuarioBodyScreen(
        uiState = uiState,
        goToBack = goToBack,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}

@Composable
fun EditarUsuarioBodyScreen(
    uiState: ClienteUiState,
    goToBack: () -> Unit,
    onEvent: (AuthEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.success) {
        uiState.success?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    LaunchedEffect(Unit) {
        val emailUsuario = FirebaseAuth.getInstance().currentUser?.email
        if (emailUsuario != null) {
            onEvent(AuthEvent.UpdateUsuario(emailUsuario))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {

            Text(
                text = "Información Personal",
                style = typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.nombre,
                        onValueChange = { onEvent(AuthEvent.OnchangeNombre(it)) },
                        label = { Text("Nombre") },
                        placeholder = { Text("Ej: Juan") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )
                    Text(
                        text = uiState.errorNombre,
                        color = colorScheme.error,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.apellidos,
                        onValueChange = { onEvent(AuthEvent.OnchangeApellidos(it)) },
                        label = { Text("Apellidos") },
                        placeholder = { Text("Ej: Pérez") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = uiState.errorApellidos,
                        color = colorScheme.error,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column {
                PhoneInputField(
                    phone = uiState.celular,
                    onPhoneChange = { onEvent(AuthEvent.OnchangeCelular(it)) },
                )
                Text(
                    text = uiState.errorCelular,
                    color = colorScheme.error,
                    style = typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(AuthEvent.OnChangeEmail(it)) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = uiState.direccion,
                        onValueChange = { onEvent(AuthEvent.OnchangeDireccion(it)) },
                        label = { Text("Dirección") },
                        placeholder = { Text("Ej: Calle Principal #123") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = uiState.errorDireccion,
                        color = colorScheme.error,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    CedulaInputField(
                        cedula = uiState.cedula,
                        onCedulaChange = { onEvent(AuthEvent.OnchangeCedula(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = uiState.errorCedula,
                        color = colorScheme.error,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Text(
                text = uiState.error?:"",
                color = colorScheme.error,
                style = typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            OutlinedButton(
                onClick = { onEvent(AuthEvent.UpdateClient) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar Cambios")
                Text(text = "Guardar Cambios", modifier = Modifier.padding(start = 8.dp))
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        FloatingActionButton(
            onClick = goToBack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 16.dp),
            containerColor = Color.LightGray
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }
    }
}