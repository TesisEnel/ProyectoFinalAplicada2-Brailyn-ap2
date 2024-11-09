package ucne.edu.proyectofinalaplicada2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ucne.edu.proyectofinalaplicada2.presentation.navigation.Screen

@Composable
fun ModalDrawerSheet(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    navHostController: NavHostController
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxWidth(0.60f)  // Reducir el ancho
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .height(150.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                "Menú",
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.Center),
                fontSize = 50.sp
            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Home",
                    fontSize = 18.sp
                )
            },
            selected = false,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                navHostController.navigate(Screen.Home) {
                    popUpTo(0)
                }
            },
        )
    }
}

