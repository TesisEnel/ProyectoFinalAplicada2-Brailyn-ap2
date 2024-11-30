package ucne.edu.proyectofinalaplicada2.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import ucne.edu.proyectofinalaplicada2.components.NavigationBar
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthEvent
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthViewModel
import ucne.edu.proyectofinalaplicada2.presentation.renta.RentaListSceen
import ucne.edu.proyectofinalaplicada2.presentation.renta.RentaScreen
import ucne.edu.proyectofinalaplicada2.presentation.tipovehiculo.TipoVehiculeListScreen
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoRegistroScreen
import ucne.edu.proyectofinalaplicada2.presentation.view.FiltraVehiculo
import ucne.edu.proyectofinalaplicada2.presentation.view.Home
import ucne.edu.proyectofinalaplicada2.ui.theme.ProyectoFinalAplicada2Theme

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    mainViewModel: NavHostViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    MainBodyNavHost(
        navHostController = navHostController,
        onEvent = { event -> mainViewModel.onEvent(event) },
        uiState = uiState,
        onEventAuth = { event -> authViewModel.onEvent(event) }
    )
}



@SuppressLint("NewApi")
@Composable
fun MainBodyNavHost(
    navHostController: NavHostController,
    onEvent: (MainEvent) -> Unit = {},
    uiState: MainUiState,
    viewModel: AuthViewModel = hiltViewModel(),
    onEventAuth: (AuthEvent) -> Unit

) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val AuthUiState by viewModel.uistate.collectAsStateWithLifecycle()

    // Verificar si el usuario es admin al cargar
    LaunchedEffect(Unit) {
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (email != null) {
            onEventAuth(AuthEvent.CheckIfUserIsAdmin(email))// Aquí se llama
        }
    }
    Scaffold(
        bottomBar = {
            NavigationBar(
                navHostController = navHostController,
                selectedItemIndex = selectedItemIndex,
                onSelectItem = { selectedIndex -> selectedItemIndex = selectedIndex },
                uistate = AuthUiState
            )
        },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 45.dp,
                            bottomStart = 45.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, end = 16.dp, start = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier

                    ) {
                        Image(
                            painter =  rememberAsyncImagePainter(uiState.userPhotoUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp) // Tamaño de la imagen
                                .padding(end = 8.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                        )
                        AnimatedText(
                            text = uiState.currentTitle,
                            modifier = Modifier
                        )
                    }
                    Row {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Filled.Person, contentDescription = "Sign Out")
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
                }
            }
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
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Home(
                        onGoVehiculeList = {
                            navHostController.navigate(Screen.TipoVehiculoListScreen(it))
                        },
                        onGoSearch = {
                            navHostController.navigate(Screen.FiltraVehiculo)
                        }
                    )
                }

                composable<Screen.VehiculoRegistroScreen> {
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        VehiculoRegistroScreen()
                    }
                }

                composable<Screen.TipoVehiculoListScreen> {
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val id = it.toRoute<Screen.TipoVehiculoListScreen>().id
                        TipoVehiculeListScreen(
                            onGoVehiculePresentation = {vehiculoId ->
                                navHostController.navigate(Screen.RentaScreen(vehiculoId))
                            },
                            marcaId = id,
                        )
                    }
                }

                composable<Screen.RentaScreen> {
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val id = it.toRoute<Screen.RentaScreen>().id
                        RentaScreen(
                            vehiculoId = id
                        )
                    }
                }
                composable<Screen.RentaListScreen> {
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        RentaListSceen()
                    }
                }
                composable<Screen.FiltraVehiculo> {
                    onEvent(MainEvent.UpdateCurrentRoute(backStackEntry?: return@composable))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        FiltraVehiculo()
                    }
                }
            }
        }
    }
}


@Composable
fun AnimatedText(text: String, modifier: Modifier = Modifier) {
    var displayedText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        displayedText = ""
        text.forEachIndexed { index, _ ->
            displayedText = text.substring(0, index + 1)
            delay(30)
        }
    }
    Text(
        text = displayedText,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 16.sp,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun NavHostController() {
    ProyectoFinalAplicada2Theme {
        val navController = rememberNavController()
        MainNavHost(navHostController = navController)

    }
}