package ucne.edu.proyectofinalaplicada2.presentation.vehiculo

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import ucne.edu.proyectofinalaplicada2.components.InputSelect
import ucne.edu.proyectofinalaplicada2.presentation.permisos.PermisoGallery
import java.io.File

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

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            onOptionSelected = { onEnvent(VehiculoEvent.OnChangeMarcaId(it.proveedorId)) },
            labelSelector = { it.nombre }
        )

        OutlinedTextField(
            value = uistate.precio.toString(),
            onValueChange = { onEnvent(VehiculoEvent.OnChangePrecio(it.toInt())) },
            label = { Text(text = "Precio") }
        )


        OutlinedTextField(
            value = uistate.descripcion.toString(),
            onValueChange = { onEnvent(VehiculoEvent.OnChangeDescripcion(it)) },
            label = { Text(text = "Descripcion") }
        )
        SelectSingleImage()

        PermisoGallery()
        OutlinedButton(onClick = { onEnvent(VehiculoEvent.Save) }) {
            Text(text = "Guardar")
        }

        uistate.error.let { error ->
            Text(text = error, color = androidx.compose.ui.graphics.Color.Red)
        }

    }
}

@Composable
fun SelectSingleImage() {
    val viewModel: VehiculoViewModel = hiltViewModel()
    val uistate by viewModel.uistate.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val selectedImage =
        remember { mutableStateOf<Uri?>(null) }  // Variable para guardar la imagen seleccionada

    // Lanzador para seleccionar una sola imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImage.value = it  // Guardar el URI de la imagen seleccionada

            // Convertir el URI en un archivo y enviarlo al ViewModel
            val file = uriToFile(it, context)
            file?.let { imageFile ->
                viewModel.onEvent(VehiculoEvent.OnChangeImagePath(imageFile))
            }
        }
    }

    // Función para abrir la galería
    fun openGallery() {
        imagePickerLauncher.launch("image/*")  // Filtra solo imágenes
    }

    OutlinedButton(onClick = {
        openGallery()
    }) {
        Text(text = "Seleccionar Imagen")
    }
    // Mostrar la imagen seleccionada
    selectedImage.value?.let { uri ->
        Image(
            painter = rememberAsyncImagePainter(uri),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
        )
    }
}

// Función para convertir el URI en un archivo
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


