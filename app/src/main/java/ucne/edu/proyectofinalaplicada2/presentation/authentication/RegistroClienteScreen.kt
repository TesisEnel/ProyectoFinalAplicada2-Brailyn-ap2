package ucne.edu.proyectofinalaplicada2.presentation.authentication


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.presentation.components.CedulaVisualTransformation
import ucne.edu.proyectofinalaplicada2.presentation.components.PhoneNumberVisualTransformation


@Composable
fun RegistroClienteScreen(
    goToBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),

    ) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()
    RegistroClienteBodyScreen(
        uiState = uiState,
        goToBack = goToBack,
        onEvent = { event -> viewModel.onEvent(event) },
    )
}

@Composable
fun RegistroClienteBodyScreen(
    uiState: ClienteUiState,
    goToBack: () -> Unit,
    onEvent: (AuthEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Registro de Usuario",
                style = MaterialTheme.typography.titleLarge,
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
                style = MaterialTheme.typography.bodyMedium,
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
                style = MaterialTheme.typography.bodyMedium,

                )
            Spacer(modifier = Modifier.height(8.dp))
            PhoneInputField(
                phone = uiState.celular,
                onPhoneChange = { onEvent(AuthEvent.OnchangeCelular(it)) }
            )

            Text(
                text = uiState.errorCelular,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
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
                style = MaterialTheme.typography.bodyMedium,

                )
            Spacer(modifier = Modifier.height(8.dp))


            CedulaInputField(
                cedula = uiState.cedula,
                onCedulaChange = { onEvent(AuthEvent.OnchangeCedula(it)) }
            )
            Text(
                text = uiState.errorCedula,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(AuthEvent.OnChangeEmail(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = uiState.errorEmail ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(AuthEvent.OnChangePassword(it)) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.errorPassword ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    onEvent(AuthEvent.Signup)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Registrar")
                Text(text = "Registrar", modifier = Modifier.padding(start = 8.dp))
            }
        }

        FloatingActionButton(
            onClick = goToBack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }

    }
}

@Composable
fun PhoneInputField(
    phone: String,
    onPhoneChange: (String) -> Unit
) {
    OutlinedTextField(
        value = phone,
        onValueChange = {
            val onlyDigits = it.filter { char -> char.isDigit() }
            if (onlyDigits.length <= 10) {
                onPhoneChange(onlyDigits)
            }
        },
        visualTransformation = PhoneNumberVisualTransformation(),
        label = { Text("Número de Teléfono") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CedulaInputField(
    cedula: String,
    onCedulaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = cedula,
        onValueChange = {
            val onlyDigits = it.filter { char -> char.isDigit() }
            if (onlyDigits.length <= 11) {
                onCedulaChange(onlyDigits)
            }
        },
        visualTransformation = CedulaVisualTransformation(),
        label = { Text("Cédula") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier.fillMaxWidth()
    )
}