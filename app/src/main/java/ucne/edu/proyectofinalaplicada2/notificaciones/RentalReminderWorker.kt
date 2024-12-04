package ucne.edu.proyectofinalaplicada2.notificaciones

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ucne.edu.proyectofinalaplicada2.R

class RentalReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val vehiculoId = inputData.getInt("vehiculoId", -1)
        val vehiculoName = inputData.getString("vehiculoName") ?: "Vehículo"
        val notificationType = inputData.getString("notificationType") ?: "return"

        val notificationTitle = if (notificationType == "pickup") {
            "¡Recordatorio de recogida!"
        } else {
            "¡Recordatorio de renta!"
        }

        val notificationContent = if (notificationType == "pickup") {
            "Faltan 1-2 días para recoger el vehículo: $vehiculoName."
        } else {
            "Quedan 3 días para devolver el vehículo: $vehiculoName."
        }

        val notification = NotificationCompat.Builder(applicationContext, "rental_reminders")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(applicationContext).notify(vehiculoId, notification)
        }

        return Result.success()
    }
}