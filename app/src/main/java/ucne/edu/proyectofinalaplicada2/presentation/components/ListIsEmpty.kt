package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.utils.Constant

@Composable
fun ListIsEmpty() {
    val painter = rememberAsyncImagePainter(Constant.URL_BLOBSTORAGE + "emptyList.jpg")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = "No hay vehiculos",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp),
            )
        }
    }
}