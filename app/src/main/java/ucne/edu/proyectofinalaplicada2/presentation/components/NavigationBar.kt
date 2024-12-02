package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.presentation.navigation.BottomNavigationItem
import ucne.edu.proyectofinalaplicada2.presentation.navigation.Screen

@Composable

fun NavigationBar(
    navHostController: NavHostController,
    selectedItemIndex: Int,
    onSelectItem: (Int) -> Unit,
    roleFlow: Flow<Boolean>
) {
    val isAdmin by roleFlow.collectAsStateWithLifecycle(false)
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        val items = if (isAdmin) {
            listOf(
                BottomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unSelectedIcon = Icons.Outlined.Home,
                    screen = Screen.Home
                ),
                BottomNavigationItem(
                    title = "Vehiculo",
                    selectedIcon = ImageVector.vectorResource(id = R.drawable.car_back),
                    unSelectedIcon = Icons.Outlined.Call,
                    screen = Screen.VehiculoRegistroScreen()
                ),
                BottomNavigationItem(
                    title = "Historial",
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings,
                    screen = Screen.RentaListScreen
                )
            )
        } else {
            listOf(
                BottomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unSelectedIcon = Icons.Outlined.Home,
                    screen = Screen.Home
                ),
                BottomNavigationItem(
                    title = "Historial",
                    selectedIcon = Icons.Filled.Settings,
                    unSelectedIcon = Icons.Outlined.Settings,
                    screen = Screen.RentaListScreen
                )
            )
        }

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    onSelectItem(index)
                    navHostController.navigate(item.screen)
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else {
                            item.unSelectedIcon
                        },
                        contentDescription = null
                    )
                }
            )
        }
    }


}