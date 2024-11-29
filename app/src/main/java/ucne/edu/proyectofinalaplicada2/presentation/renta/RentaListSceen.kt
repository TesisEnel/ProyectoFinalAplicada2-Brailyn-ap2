package ucne.edu.proyectofinalaplicada2.presentation.renta

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ucne.edu.proyectofinalaplicada2.ui.theme.ProyectoFinalAplicada2Theme

@Composable
fun RentaListSceen(
    viewModel: RentaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()

    RentaListBodyScreen(
        uiState = uiState,
    )
}

@Composable
fun RentaListBodyScreen(
    uiState: RentaUistate,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Rentas",
            modifier = Modifier
                .fillMaxWidth(),
            fontWeight = FontWeight.W500,
            fontSize = 32.sp
        )
        Text(
            text = "Recuerda que puedes expandir para ver mas detalles",
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 15.sp
        )
        ExpandableCard(uiState = uiState)
    }
}

@Composable
fun ExpandableCard(
    uiState: RentaUistate
) {
    var isExpanded by remember { mutableStateOf<Int?>(null)  }

    uiState.rentas.forEachIndexed{index, _ ->
        ExpandableCard(
            uiState = uiState,
            isExpanded = isExpanded == index,
            onCardArrowClick = { isExpanded = if (isExpanded == index) null else index }
        )
    }

}

@Composable
fun ExpandableCard(
    uiState: RentaUistate,
    isExpanded: Boolean,
    onCardArrowClick: () -> Unit
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
                    text = "Marca",
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
                        text = "Modelo: \nAño: \nTotal: ${uiState.total}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Renta: \nEntrega: ",
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
