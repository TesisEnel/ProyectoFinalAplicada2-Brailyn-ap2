package ucne.edu.proyectofinalaplicada2.components

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.auth.GoogleAuthClient
import ucne.edu.proyectofinalaplicada2.presentation.navigation.Screen


@Composable
fun AuthClient(
    applicationContext: Context,
) {
    val googleAuthClient = GoogleAuthClient(applicationContext.applicationContext)
    val navHostController = rememberNavController()
    val scope = rememberCoroutineScope()
    var isSignedIn by rememberSaveable {
        mutableStateOf(googleAuthClient.isSignedIn())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopEnd

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (isSignedIn) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            googleAuthClient.signOut()
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
            } else {
                Text(
                    text = "Log in",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                )

                OutlinedTextField(
                    value = "uistate.email",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = "uistate.contraseña",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Password Icon"
                        )
                    },
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            googleAuthClient.signIn()
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Iniciar Sesion",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // <-- Aquí está la clave
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "OR",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }


                OutlinedButton(
                    onClick = {
                        scope.launch {
                            googleAuthClient.signIn()

                        }
                        navHostController.navigate(Screen.Home)
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Icon(
                       painter = painterResource(id = R.drawable.google_color_svgrepo_com),
                        tint = Color.Unspecified,
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign In With Google",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 16.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
