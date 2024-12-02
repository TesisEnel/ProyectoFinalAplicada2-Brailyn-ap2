package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.proyectofinalaplicada2.presentation.authentication.AuthViewModel
import ucne.edu.proyectofinalaplicada2.ui.theme.ProyectoFinalAplicada2Theme

@Composable
fun RentaListSceen(
    viewModel: RentaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()

    if(uiState.isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else{
        RentaListBodyScreen(
            uiState = uiState,
            onEvent = {event -> viewModel.onEvent(event)}
        )
    }
}

@Composable
fun RentaListBodyScreen(
    uiState: RentaUistate,
    onEvent: (RentaEvent) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Rentas",
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.W500,
                fontSize = 32.sp
            )
        }
        item {
            Text(
                text = "Recuerda que puedes expandir para ver mas detalles",
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 15.sp
            )
        }
        item {
            ExpandableCard(
                uiState = uiState,
                onEvent =  onEvent
            )
        }
    }
}

@Composable
fun ExpandableCard(
    uiState: RentaUistate,
    authViewModel: AuthViewModel = hiltViewModel(),
    onEvent: (RentaEvent) -> Unit = {}
) {
    val rentaUiState by authViewModel.uistate.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf<Int?>(null)  }
    LaunchedEffect(Unit) {
        if(!rentaUiState.isAdmin){
            onEvent(RentaEvent.MostraDatosVehiculoByRole(false))
        }
        else{
            onEvent(RentaEvent.MostraDatosVehiculo)
        }
    }
    uiState.rentaConVehiculos.forEachIndexed{ index, renta ->
        ExpandableBodyCard(
            isExpanded = isExpanded == index,
            onCardArrowClick = { isExpanded = if (isExpanded == index) null else index },
            rentaConVehiculo = renta
        )
    }
}

@Composable
fun ExpandableBodyCard(
    isExpanded: Boolean,
    onCardArrowClick: () -> Unit,
    rentaConVehiculo: RentaConVehiculo
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rentaConVehiculo.marca?.nombreMarca?:"",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp
                )
                IconButton(onClick = onCardArrowClick) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Colapsar" else "Expandir"
                    )
                }
            }
            if (isExpanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Modelo:${rentaConVehiculo.nombreModelo} \nAño:${rentaConVehiculo.anio} \nTotal: ${rentaConVehiculo.renta?.total}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Renta: ${rentaConVehiculo.renta?.fechaRenta} \nEntrega: ${rentaConVehiculo.renta?.fechaEntrega} ",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RentaListBodyScreenPreview() {
    ProyectoFinalAplicada2Theme {
        RentaListBodyScreen(
            uiState = RentaUistate()
        )
    }
}
