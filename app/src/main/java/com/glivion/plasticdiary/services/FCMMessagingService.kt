package com.glivion.plasticdiary.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.util.FCM_DATA
import com.glivion.plasticdiary.util.FCM_TITLE
import com.glivion.plasticdiary.util.REMOTE_MSG
import com.glivion.plasticdiary.util.getTimeStamp
import com.glivion.plasticdiary.view.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FCMMessagingService: FirebaseMessagingService() {
    private lateinit var broadcastManager: LocalBroadcastManager
    override fun onCreate() {
        super.onCreate()
        broadcastManager = LocalBroadcastManager.getInstance(this)
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.e("token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        handleNotification(remoteMessage)
    }

    private fun handleNotification(remoteMessage: RemoteMessage) {
        val intentPend = Intent(
            applicationContext,
            MainActivity::class.java
        )
        intentPend.putExtra(FCM_TITLE, remoteMessage.notification!!.title)
        intentPend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intentPend,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelID = "Announcements"
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            baseContext, channelID
        )
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.plastic_diary)
            .setContentIntent(pendingIntent)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(remoteMessage.notification!!.body)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(defaultSoundUri)

        val notificationManager =
            baseContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val id = getTimeStamp().toEpochSecond().toInt()
        notificationManager.notify(id, notificationBuilder.build())

        val intent = Intent(FCM_DATA)
        intent.putExtra(REMOTE_MSG, remoteMessage)
        broadcastManager.sendBroadcast(intent)
    }
}