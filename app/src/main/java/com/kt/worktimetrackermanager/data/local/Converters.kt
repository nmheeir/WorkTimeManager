package com.kt.worktimetrackermanager.data.local

import androidx.room.TypeConverter
import com.kt.worktimetrackermanager.data.remote.dto.enums.NotificationType
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    fun notificationTypeToString(value: NotificationType?): String? {
        return value?.name
    }

    @TypeConverter
    fun stringToNotificationType(value: String?): NotificationType? {
        return value?.let { NotificationType.valueOf(it) }
    }

}