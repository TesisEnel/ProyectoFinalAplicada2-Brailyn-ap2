package ucne.edu.proyectofinalaplicada2.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ucne.edu.proyectofinalaplicada2.presentation.navigation.BottomNavigationItem
import ucne.edu.proyectofinalaplicada2.presentation.navigation.Screen

@Composable

fun NavigationBar(
    navHostController: NavHostController,
    selectedItemIndex: Int,
    onSelectItem: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        val items = listOf(
            BottomNavigationItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unSelectedIcon = Icons.Outlined.Home,
                screen = Screen.Home
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