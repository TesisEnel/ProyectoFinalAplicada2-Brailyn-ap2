package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.components.InputSelect
import ucne.edu.proyectofinalaplicada2.presentation.permisos.PermisoGallery
import java.io.File
import androidx.compose.ui.graphics.Color

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun VehiculoRegistroScreen(
    vehiculoViewModel: VehiculoViewModel = hiltViewModel(),
    onBackHome: () -> Unit
) {
    val uistate by vehiculoViewModel.uistate.collectAsStateWithLifecycle()

    VehiculoBodyRegistroScreen(
        uistate = uistate,
        onBackHome = onBackHome,
        onEnvent = { vehiculoEvent -> vehiculoViewModel.onEvent(vehiculoEvent) }
    )


}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun VehiculoBodyRegistroScreen(
    uistate: Uistate,
    onBackHome: () -> Unit,
    onEnvent: (VehiculoEvent) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 45.dp, vertical = 30.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 25.dp)
        ) {
            AsyncImage(
                model = "https://rentcarblobstorage.blob.core.windows.net/images/carlogo.png",
                contentDescription = null
            )
            InputSelect(
                label = "Marca",
                options = uistate.marcas,
                onOptionSelected = {
                    onEnvent(VehiculoEvent.OnChangeMarcaId(it.marcaId))
                },
                labelSelector = { it.nombreMarca }
            )

            InputSelect(
                label = "Tipo Combustible",
                options = uistate.tiposCombustibles,
                onOptionSelected = {
                    onEnvent(VehiculoEvent.OnChangeTipoCombustibleId(it.tipoCombustibleId))
                },
                labelSelector = { it.nombreTipoCombustible }
            )

            InputSelect(
                label = "Tipo Vehiculo",
                options = uistate.tiposVehiculos,
                onOptionSelected = { onEnvent(VehiculoEvent.OnChangeTipoVehiculoId(it.tipoVehiculoId)) },
                labelSelector = { it.nombreTipoVehiculo }
            )

            InputSelect(
                label = "Modelo",
                options = uistate.modelos,
                onOptionSelected = { onEnvent(VehiculoEvent.OnChangeModeloId(it.modeloId)) },
                labelSelector = { it.modeloVehiculo }
            )

            InputSelect(
                label = "Proveedor",
                options = uistate.proveedores,
                onOptionSelected = { onEnvent(VehiculoEvent.OnChangeProveedorId(it.proveedorId)) },
                labelSelector = { it.nombre }
            )

            OutlinedTextField(
                value = uistate.precio?.toString() ?: "",
                onValueChange = { onEnvent(VehiculoEvent.OnChangePrecio(it.toInt())) },
                label = { Text(text = "Precio") },
                modifier = Modifier
                    .fillMaxWidth(),
                )
            OutlinedTextField(
                value = uistate.anio?.toString() ?: "",
                onValueChange = { onEnvent(VehiculoEvent.OnChangeAnio(it.toInt())) },
                label = { Text(text = "Año") },
                modifier = Modifier
                    .fillMaxWidth(),
                )


            OutlinedTextField(
                value = uistate.descripcion,
                onValueChange = { onEnvent(VehiculoEvent.OnChangeDescripcion(it)) },
                label = { Text(text = "Descripcion") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            PermisoGallery()
            OutlinedButton(onClick = { onEnvent(VehiculoEvent.Save) }) {
                Text(text = "Guardar")
            }
            if(uistate.isLoadingData == true){
                LinearProgressIndicator()
            }


            Text(text = uistate.error, color = Color.Red)
        }
    }
}

@Composable
fun SelectMultipleImages() {
    val viewModel: VehiculoViewModel = hiltViewModel()
    val context = LocalContext.current

    val selectedImages = remember { mutableStateListOf<Uri>() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages.clear()
            selectedImages.addAll(uris)

            val imageFiles = uris.mapNotNull { uri ->
                uriToFile(uri, context)
            }
            if (imageFiles.isNotEmpty()) {
                viewModel.onEvent(VehiculoEvent.OnChangeImagePath(imageFiles)) // Asumiendo que el evento recibe una lista de archivos
            }
        }
    }

    OutlinedButton(onClick = {
        imagePickerLauncher.launch("image/*")
    }) {
        Text(text = "Seleccionar Imágenes")
    }

    if (selectedImages.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImages) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}


fun uriToFile(uri: Uri, context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("selected_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


