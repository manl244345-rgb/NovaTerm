package com.novaterm.app.terminal

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.novaterm.app.R

/**
 * Foreground service that keeps terminal sessions alive when the app is backgrounded.
 */
class TerminalService : Service() {

    companion object {
        const val CHANNEL_ID = "novaterm_terminal"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START = "com.novaterm.app.TERMINAL_START"
        const val ACTION_STOP = "com.novaterm.app.TERMINAL_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
                START_NOT_STICKY
            }
            else -> START_STICKY
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Terminal Sessions",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Keeps terminal sessions running in the background"
            setShowBadge(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("NovaTerm")
            .setContentText("Terminal session active")
            .setSmallIcon(android.R.drawable.ic_menu_manage)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}
