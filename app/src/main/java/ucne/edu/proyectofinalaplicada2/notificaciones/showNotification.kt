package ucne.edu.proyectofinalaplicada2.notificaciones

import android.annotation.SuppressLint
import android.app.Activity.NOTIFICATION_SERVICE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import ucne.edu.proyectofinalaplicada2.R

@SuppressLint("MissingPermission")
fun showNotification(
    context: Context,
    channelId: String,
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.logo)
        .setContentTitle("RentCar")
        .setContentText("Este es un ejemplo de notificación")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}

fun createNotificationChannel(context: Context, channelId: String ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val descriptionText = "RentCar"
        val channel = NotificationChannel(
            channelId,
            "RentCar",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService( NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}