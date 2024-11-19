package ucne.edu.proyectofinalaplicada2.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ucne.edu.proyectofinalaplicada2.components.AuthClient
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoRegistroScreen
import ucne.edu.proyectofinalaplicada2.presentation.view.Home
import ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo.TipoVehiculeListScreen
import ucne.edu.proyectofinalaplicada2.presentation.view.VehiculePresentation

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentCarNavHost(
    navHostController: NavHostController,
) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BravquezRentcar") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )

            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unSelectedIcon = Icons.Outlined.Home,
                        screen =  Screen.Home
                    ),
                    BottomNavigationItem(
                        title = "Renta",
                        selectedIcon = Icons.Filled.Call,
                        unSelectedIcon = Icons.Outlined.Call,
                        screen = Screen.VehiculoRegistroScreen

                    ),
                    BottomNavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unSelectedIcon = Icons.Outlined.Settings,
                        screen = Screen.Home

                    )
                )

                items.forEachIndexed{index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex ==index,
                        onClick = {
                            selectedItemIndex =index
                            navHostController.navigate(item.screen)

                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon ={
                            Icon(
                                imageVector = if(index == selectedItemIndex){
                                    item.selectedIcon
                                }else item.unSelectedIcon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    ) { innerpadding ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.AuthScreen,
            modifier = Modifier
                .padding(innerpadding)
        ) {
            composable<Screen.Home> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Home(
                        onGoVehiculePresentation = {navHostController.navigate(Screen.TipoVehiculoListScreen(it))}
                    )
                }
            }
            composable<Screen.VehiculoRegistroScreen> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    VehiculoRegistroScreen(
                        onBackHome = {}
                    )
                }

            }
            composable<Screen.AuthScreen> {
                composable<Screen.AuthScreen> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.weight(.2f))

                        AuthClient(navHostController.context)

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

            }
            composable<Screen.VehiculePresentation> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    val id = it.toRoute<Screen.VehiculePresentation>().id
                    VehiculePresentation(
                        onBack = {},
                        onCreateRenta = {},
                        vehiculoId = id
                    )
                }

            }
            composable<Screen.TipoVehiculoListScreen> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    val id = it.toRoute<Screen.VehiculePresentation>().id
                    TipoVehiculeListScreen(
                        onBack = {},
                        onCreateRenta = {},
                        vehiculoId = id
                    )
                }

            }
        }
    }
}