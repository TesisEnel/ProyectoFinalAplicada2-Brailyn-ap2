@file:JvmName("GoogleAuthClientKt")

package ucne.edu.proyectofinalaplicada2.presentation.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.presentation.components.CustomDialog
import ucne.edu.proyectofinalaplicada2.ui.theme.Gradiend

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigationLogin: () -> Unit,
) {
    val uiState by viewModel.uistate.collectAsState()
    val scope = rememberCoroutineScope()
    val onEvent: (AuthEvent) -> Unit = { event: AuthEvent ->
        viewModel.onEvent(event)
    }
    var passwordVisibility by remember { mutableStateOf(false) }
    var onDismiss by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.run {
                linearGradient(
                    colors = listOf(Color(0xFF645455), Color(0xFF4A28BA)),
                    start = Offset(1922f, 220f),
                    end = Offset.Infinite
                )
            }),
        contentAlignment = Alignment.Center,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(AuthEvent.OnChangeEmail(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email Icon",
                        tint = Gradiend
                    )
                },
                label = { Text("Email", color = Gradiend) }
            )


            PasswordTextField(
                password = uiState.password,
                onPasswordChange = { onEvent(AuthEvent.OnChangePassword(it)) },
                passwordVisibility = passwordVisibility,
                onTogglePasswordVisibility = { passwordVisibility = !passwordVisibility }
            )
            OutlinedButton(
                onClick = {
                    onEvent(AuthEvent.ClearError)
                    onEvent(AuthEvent.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                ) {
                Text("Iniciar sesión", color = Color.White)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Gradiend
                )
                Text(
                    text = "O",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gradiend
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Gradiend
                )
            }
            OutlinedButton(
                onClick = {
                    scope.launch {
                        onEvent(AuthEvent.SignInWithGoogle)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_color_svgrepo_com),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar sesión con Google", color = Color.White)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom

            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿No tienes cuenta?", color = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Regístrate!",
                        color = Color.Yellow,
                        modifier = Modifier.clickable {
                            onNavigationLogin()

                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }

            if(uiState.error?.isNotBlank() == true){
                CustomDialog(
                    message = uiState.error!!,
                    isError = uiState.error?.isNotBlank() ==true,
                    onDismiss = {
                        onDismiss = true
                        onEvent(AuthEvent.ClearError)
                    }
                )
                onDismiss = false
            }
        }

    }
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisibility: Boolean,
    onTogglePasswordVisibility: () -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        label = { Text("Password", color = Gradiend) },
        leadingIcon = { PasswordLeadingIcon() },
        trailingIcon = { PasswordTrailingIcon(passwordVisibility, onTogglePasswordVisibility) },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}


@Composable
fun PasswordLeadingIcon() {
    Icon(
        imageVector = Icons.Filled.Lock,
        contentDescription = "Password Icon",
        tint = Gradiend
    )
}

@Composable
fun PasswordTrailingIcon(
    passwordVisibility: Boolean,
    onTogglePasswordVisibility: () -> Unit,
) {
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(
            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
            tint = Gradiend
        )
    }
}