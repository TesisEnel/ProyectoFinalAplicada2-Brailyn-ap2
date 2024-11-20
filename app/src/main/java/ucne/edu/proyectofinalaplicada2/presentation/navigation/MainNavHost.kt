package ucne.edu.proyectofinalaplicada2.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import ucne.edu.proyectofinalaplicada2.components.NavigationBar
import ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo.TipoVehiculeListScreen
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoRegistroScreen
import ucne.edu.proyectofinalaplicada2.presentation.view.Home
import ucne.edu.proyectofinalaplicada2.presentation.view.VehiculePresentation

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(navHostController: NavHostController) {
    // Pantallas con Scaffold
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                navHostController = navHostController,
                selectedItemIndex = selectedItemIndex,
                onSelectItem = { selectedIndex -> selectedItemIndex = selectedIndex }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BravquezRentcar") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Sign Out")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sign Out") },
                            onClick = {
                                FirebaseAuth.getInstance().signOut()

                            }
                        )
                    }
                }
            )
        },

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Screen.Home
            ) {
                composable<Screen.Home> {
                    Home(
                        onGoVehiculePresentation = {
                            navHostController.navigate(Screen.TipoVehiculoListScreen)
                        }
                    )
                }

                composable<Screen.VehiculoRegistroScreen> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        VehiculoRegistroScreen(onBackHome = {})
                    }
                }

                composable<Screen.TipoVehiculoListScreen> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TipoVehiculeListScreen(
                            onBack = {},
                            onCreateRenta = {},
                            vehiculoId = 0 // Agregar lógica si hay parámetros
                        )
                    }
                }

                composable<Screen.VehiculePresentation> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        VehiculePresentation(
                            onBack = {},
                            onCreateRenta = {},
                            vehiculoId = 0 // Agregar lógica si hay parámetros
                        )
                    }
                }


            }
        }
    }
}