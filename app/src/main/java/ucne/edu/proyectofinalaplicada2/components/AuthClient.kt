package ucne.edu.proyectofinalaplicada2.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.auth.GoogleAuthClient
import androidx.lifecycle.lifecycleScope

@Composable
fun AuthClient(
    applicationContext: Context,

    ) {
    val googleAuthClient = GoogleAuthClient(applicationContext.applicationContext)

    val scope = rememberCoroutineScope()
    var isSignedIn by rememberSaveable  {
        mutableStateOf(googleAuthClient.isSignedIn())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isSignedIn) {
            OutlinedButton(
                onClick = {

                    scope.launch {
                        googleAuthClient.signOut()
                    }
                }
            ) {
                Text(
                    text = "Sign Out",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp, horizontal = 16.dp
                        )
                )
            }
        } else {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        googleAuthClient.signIn()
                    }
                }
            ) {
                Text(
                    text = "Sign In With Google",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp, horizontal = 16.dp
                        )
                )
            }
        }
    }
}