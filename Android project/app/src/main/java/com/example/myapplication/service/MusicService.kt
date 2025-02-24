package com.example.myapplication.service

import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "MusicServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Создаём уведомление и запускаем Foreground Service
        val notification = createNotification()
        startForeground(1, notification)

        when (intent?.action) {
            "START" -> startMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
        }
        return START_STICKY
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                val assetFileDescriptor = application.assets.openFd("Sovereign.mp3")
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepare()
                start()
            }
        } else {
            mediaPlayer?.start()
        }

        // Проверяем, можно ли обновлять уведомления
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = createNotification()
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.notify(1, notification)
        }
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()

        // Проверяем, можно ли обновлять уведомления
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = createNotification("Music Paused", "Tap to resume")
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.notify(1, notification)
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(title: String = "Music Playing", content: String = "Tap to return to app"): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        if (manager != null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
