package ucne.edu.proyectofinalaplicada2.presentation.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth

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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Editar Usuario",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { onEvent(AuthEvent.OnchangeNombre(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.errorNombre,
                color = Color.Red,
                style = typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.apellidos,
                onValueChange = { onEvent(AuthEvent.OnchangeApellidos(it)) },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.errorApellidos,
                color = Color.Red,
                style = typography.bodyMedium,

                )
            Spacer(modifier = Modifier.height(8.dp))

            PhoneInputField(
                phone = uiState.celular,
                onPhoneChange = { onEvent(AuthEvent.OnchangeCelular(it)) }
            )
            Text(
                text = uiState.errorCelular,
                color = Color.Red,
                style = typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = { onEvent(AuthEvent.OnchangeDireccion(it)) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.errorDireccion,
                color = Color.Red,
                style = typography.bodyMedium,

                )
            Spacer(modifier = Modifier.height(8.dp))

            CedulaInputField(
                cedula = uiState.cedula,
                onCedulaChange = { onEvent(AuthEvent.OnchangeCedula(it)) }
            )
            Text(
                text = uiState.errorCedula,
                color = Color.Red,
                style = typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(AuthEvent.OnChangeEmail(it)) },
                label = { Text("Email") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                style = typography.bodyMedium,
            )
            // Botón para guardar cambios
            OutlinedButton(
                onClick = {
                    val emailUsuario = FirebaseAuth.getInstance().currentUser?.email
                    onEvent(AuthEvent.UpdateUsuario(emailUsuario?:""))
                }, // Evento para guardar cambios
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar Cambios")
                Text(text = "Guardar Cambios", modifier = Modifier.padding(start = 8.dp))
            }
        }

        FloatingActionButton(
            onClick = goToBack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }
    }
}