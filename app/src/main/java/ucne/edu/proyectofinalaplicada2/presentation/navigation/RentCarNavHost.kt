package ucne.edu.proyectofinalaplicada2.presentation.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RentCarNavHost(
    navHostController: NavHostController,
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = remember { mutableStateOf(firebaseAuth.currentUser) }
    val isAuthenticated = derivedStateOf { currentUser.value != null }

    DisposableEffect(Unit) {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            currentUser.value = auth.currentUser
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        onDispose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    if (isAuthenticated.value) {
        MainNavHost(navHostController)
    } else {
        AuthNavHost(navHostController)
    }

}
