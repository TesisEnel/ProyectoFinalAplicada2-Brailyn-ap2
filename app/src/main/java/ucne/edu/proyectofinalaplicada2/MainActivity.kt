package ucne.edu.proyectofinalaplicada2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ucne.edu.proyectofinalaplicada2.presentation.navigation.RentCarNavHost
import ucne.edu.proyectofinalaplicada2.ui.theme.ProyectoFinalAplicada2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinalAplicada2Theme {
                val navController = rememberNavController()
                RentCarNavHost(navController)
            }
        }
    }
}
