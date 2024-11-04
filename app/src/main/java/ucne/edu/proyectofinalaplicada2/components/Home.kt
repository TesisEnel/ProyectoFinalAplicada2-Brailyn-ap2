package ucne.edu.proyectofinalaplicada2.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ucne.edu.proyectofinalaplicada2.R
import ucne.edu.proyectofinalaplicada2.ui.theme.ProyectoFinalAplicada2Theme

@Composable
fun Home() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchBar()
            VehiculesPresentation()

        }

    }

}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
            focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
    )
}

@Composable
fun VehiculesPresentation() {
    val painter = painterResource(id = R.drawable.ferrari)
    val descripcion = "Un ferrari rojo"
    val title = "Ferrari posando duro duro"
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        items(vehiculos){
            Box(
                modifier = Modifier
                    .width(200.dp)

            ) {
                ImageCard(painter = painter, contentDescription = descripcion, title = title)

            }
        }
    }


}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(text = title, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }
        }
    }
}

private val vehiculos = listOf(
    R.drawable.ferrari,
    R.drawable.ferrari,
    R.drawable.ferrari,
    R.drawable.ferrari
)

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    ProyectoFinalAplicada2Theme {
        Home()
    }
}