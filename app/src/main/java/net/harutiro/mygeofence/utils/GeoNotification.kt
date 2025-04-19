package net.harutiro.mygeofence.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import net.harutiro.mygeofence.R

class GeoNotification {
    fun showNotification(context: Context, content:String){
        val manager = NotificationManagerCompat.from(context)
        val channel = NotificationChannelCompat.Builder(
            "channel_id",
            NotificationManagerCompat.IMPORTANCE_HIGH,
        )
            .setName("位置情報に関する通知")
            .build()
        manager.createNotificationChannel (channel)
        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("お知らぜ")
            .setContentText(content)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            manager.notify(1, notification)
        }
    }
}