package ucne.edu.proyectofinalaplicada2.notificaciones

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun scheduleRentalNotification(
    context: Context,
    vehiculoId: Int,
    vehiculoName: String,
    fechaEntrega: String
) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val entregaDate = dateFormat.parse(fechaEntrega) ?: return
    val now = System.currentTimeMillis()
    val timeUntilEntrega = entregaDate.time - now

    if (timeUntilEntrega > TimeUnit.HOURS.toMillis(1)) {
        val triggerTime = if (timeUntilEntrega > TimeUnit.DAYS.toMillis(3)) {
            entregaDate.time - TimeUnit.DAYS.toMillis(3)
        } else {
            entregaDate.time - TimeUnit.HOURS.toMillis(1)
        }
        val delay = triggerTime - now

        if (delay > 0) {
            val data = Data.Builder()
                .putInt("vehiculoId", vehiculoId)
                .putString("vehiculoName", vehiculoName)
                .build()

            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<RentalReminderWorker>()
                .setInputData(data)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

fun schedulePickupNotification(
    context: Context,
    vehiculoId: Int,
    vehiculoName: String,
    fechaRenta: String
) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val rentaDate = dateFormat.parse(fechaRenta) ?: return

    val now = System.currentTimeMillis()
    val timeUntilRenta = rentaDate.time - now
    if (timeUntilRenta > TimeUnit.HOURS.toMillis(1)) {

        val triggerTime =System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)

        val delay = triggerTime - now
        if (delay > 0) {
            val data = Data.Builder()
                .putInt("vehiculoId", vehiculoId)
                .putString("vehiculoName", vehiculoName)
                .putString("notificationType", "pickup")
                .build()

            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<RentalReminderWorker>()
                .setInputData(data)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}