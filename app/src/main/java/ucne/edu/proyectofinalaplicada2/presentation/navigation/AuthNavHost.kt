package ucne.edu.proyectofinalaplicada2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ucne.edu.proyectofinalaplicada2.presentation.authentication.LoginScreen
import ucne.edu.proyectofinalaplicada2.presentation.authentication.RegistroClienteScreen

@Composable
fun AuthNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.AuthScreen
    ) {
        composable<Screen.AuthScreen> {
            LoginScreen(
                onNavigationLogin = { navHostController.navigate(Screen.RegistroClienteScreen) },
            )
        }
        composable<Screen.RegistroClienteScreen> {
            RegistroClienteScreen(
                goToBack = { navHostController.popBackStack() })
        }
    }
}