package ucne.edu.proyectofinalaplicada2.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoEvent
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.VehiculoViewModel

@Composable
fun SelectMultipleImages(
    viewModel: VehiculoViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onEvent(VehiculoEvent.OnImagesSelected(uris, context))
        }
    }

    OutlinedButton(onClick = {
        imagePickerLauncher.launch("image/*")
    }) {
        Text(text = "Seleccionar Imágenes")
    }

    val uiState by viewModel.uistate.collectAsStateWithLifecycle()

    if (uiState.imagePath.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.imagePath) { imagePath ->
                Image(
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}