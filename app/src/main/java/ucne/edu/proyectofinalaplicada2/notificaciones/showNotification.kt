package ucne.edu.proyectofinalaplicada2.notificaciones

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ucne.edu.proyectofinalaplicada2.R

@SuppressLint("MissingPermission")
     fun showNotification(
    context: Context,
    channelId: String
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