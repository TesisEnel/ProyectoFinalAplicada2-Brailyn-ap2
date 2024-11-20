package ucne.edu.proyectofinalaplicada2.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthClient

@Composable
fun AuthNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.AuthScreen
    ) {
        composable<Screen.AuthScreen> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(0.2f))

                AuthClient(
                    applicationContext = navHostController.context,

                    onNavigationLogin = { navHostController.navigate(Screen.AuthScreen) }
                )

                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}