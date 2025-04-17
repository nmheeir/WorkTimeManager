package com.kt.worktimetrackermanager.domain.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.UsernameKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.local.AppDatabase
import com.kt.worktimetrackermanager.data.local.entities.NotificationEntity
import com.kt.worktimetrackermanager.data.remote.dto.enums.NotificationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var database: AppDatabase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Token: %s", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "Thông báo"
        val body = message.notification?.body ?: ""
        val type = message.data["type"] ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val username = applicationContext.dataStore[UsernameKey]

            Timber.d("Received notification: $title, $body, $type, $username")

            if (username != null) {
                val notification = NotificationEntity(
                    receivedUsername = username,
                    title = title,
                    description = body,
                    isRead = false,
                    type = NotificationType.fromString(type),
                    receivedAt = LocalDateTime.now()
                )

                database.insert(notification)

                // Gọi lại hàm hiển thị thông báo từ UI Thread
                withContext(Dispatchers.Main) {
                    showNotification(title, body)
                }
            }
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo notification channel cho Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Thông báo chung",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.app_logo) // Thay bằng icon bạn có
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}