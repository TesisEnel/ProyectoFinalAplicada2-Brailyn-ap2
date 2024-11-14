package ucne.edu.proyectofinalaplicada2.presentation.permisos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ucne.edu.proyectofinalaplicada2.mainViewModel.MainViewModel
import ucne.edu.proyectofinalaplicada2.presentation.vehiculo.SelectSingleImage


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermisoGallery() {

    val context = LocalContext.current
    val activity = context as? Activity

    // Estado para almacenar si el permiso ha sido concedido
    val granted = remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    )}
    val viewModel = viewModel<MainViewModel>()
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val permissionsToRequest = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
    )

    // Launchers for requesting permissions
    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.READ_MEDIA_IMAGES,
                isGranted = isGranted
            )
            granted.value = isGranted
        }
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!granted.value) { // Solo muestra el botón si el permiso no ha sido concedido
            OutlinedButton(onClick = {
                cameraPermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }) {
                Text(text = "Seleccionar imagen")
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            SelectSingleImage()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Permission Dialogs
    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.READ_MEDIA_IMAGES -> GalleryPermissionTextProvider()
                    else -> return@forEach
                },
                isPermanentlyDeclined = activity?.let {
                    !shouldShowRequestPermissionRationale(it, permission)
                } ?: false,
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(arrayOf(permission))
                },
                onGoToAppSettingsClick = { activity?.openAppSettings() }
            )
        }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}





