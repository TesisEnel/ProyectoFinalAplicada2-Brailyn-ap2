package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Approval
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
    roleFlow: Flow<Boolean>,
) {
    val isAdmin by roleFlow.collectAsStateWithLifecycle(false)

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
                unSelectedIcon = ImageVector.vectorResource(id = R.drawable.car_back),
                screen = Screen.VehiculoRegistroScreen()
            ),
            BottomNavigationItem(
                title = "Historial",
                selectedIcon = Icons.Filled.Settings,
                unSelectedIcon = Icons.Outlined.Settings,
                screen = Screen.RentaListScreen
            ),
            BottomNavigationItem(
                title = "Proveedor",
                selectedIcon = Icons.Filled.Approval,
                unSelectedIcon = Icons.Outlined.Approval,
                screen = Screen.ProveedorRegistroScreen
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF645455),
                                Color(0xFF4A28BA)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset.Infinite
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                onSelectItem(index)
                                navHostController.navigate(item.screen)
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (index == selectedItemIndex) Color.White else Color.LightGray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unSelectedIcon
                                    },
                                    contentDescription = null,
                                    tint = if (index == selectedItemIndex) Color.White else Color.LightGray
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}