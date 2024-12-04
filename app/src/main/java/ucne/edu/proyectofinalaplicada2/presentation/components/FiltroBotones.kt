package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ucne.edu.proyectofinalaplicada2.presentation.modelo.ModeloEvent
import ucne.edu.proyectofinalaplicada2.presentation.modelo.ModeloFilter


@Composable
fun FiltroBotones(
    onEvent: (ModeloEvent) -> Unit,
) {
    var selectedFilter by remember { mutableStateOf<ModeloFilter>(ModeloFilter.Todos) }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 5.dp)
    ) {
        FiltroChip(
            text = "Todos",
            isSelected = selectedFilter is ModeloFilter.Todos,
            onClick = {
                selectedFilter = ModeloFilter.Todos
                onEvent(ModeloEvent.SetFilter(ModeloFilter.Todos))
            }
        )
        FiltroChip(
            text = "Disponible",
            isSelected = selectedFilter is ModeloFilter.Disponible,
            onClick = {
                selectedFilter = ModeloFilter.Disponible
                onEvent(ModeloEvent.SetFilter(ModeloFilter.Disponible))
            }
        )
        FiltroChip(
            text = "No Disponible",
            isSelected = selectedFilter is ModeloFilter.NoDisponible,
            onClick = {
                selectedFilter = ModeloFilter.NoDisponible
                onEvent(ModeloEvent.SetFilter(ModeloFilter.NoDisponible))
            }
        )
    }
}

@Composable
fun FiltroChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}