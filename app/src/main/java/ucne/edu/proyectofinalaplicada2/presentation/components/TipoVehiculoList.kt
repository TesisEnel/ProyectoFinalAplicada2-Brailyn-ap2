package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ucne.edu.proyectofinalaplicada2.presentation.marca.MarcaEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoConMarca

@Composable
fun TipoVehiculoList(
    painter: Painter,
    marca: String,
    onGoVehiculeList:(Int) -> Unit,
    vehiculoConMarca: VehiculoConMarca,
    onMarcaEvent: (MarcaEvent) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                onMarcaEvent(MarcaEvent.OnchangeMarcaId(vehiculoConMarca.vehiculo.marcaId ?: 0))
                vehiculoConMarca.vehiculo.marcaId?.let { marcaId ->
                    onGoVehiculeList(marcaId)

                }
            }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagen del vehículo
            VehiculeImage(painter = painter)

            // Nombre de la marca
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = marca,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.W500
                )
            }

            // Flecha indicando clic
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navegar",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun VehiculeImage(
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}