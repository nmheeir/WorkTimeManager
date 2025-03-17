package com.kt.worktimetrackermanager.core.presentation.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class Helper {
    companion object {

        fun isValidPassword(password: String): Boolean {
            val passwordRegex =
                "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$".toRegex()
            return passwordRegex.matches(password)
        }

        // chuyen doi createdAt sang dinh dang
        fun convertToCustomDateFormat(dateTimeString: String): String {
            val trimmedIsoString = dateTimeString.split(".")[0]
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
            val localDateTime = LocalDateTime.parse(trimmedIsoString, inputFormatter)
            return localDateTime.format(outputFormatter)
        }

        fun convertToCustomDateFormat2(isoString: String): String {
            val trimmedIsoString = isoString.split(".")[0]
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val date = LocalDateTime.parse(trimmedIsoString, inputFormatter)
            return date.format(outputFormatter)
        }
        fun convertLocalDateToISOString(localDateStr: String): String {
            val localDate = LocalDate.parse(localDateStr)
            val localDateTime = localDate.atStartOfDay()
            val zonedDateTime = localDateTime.atZone(ZoneOffset.UTC)
            return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT)
        }
    }
}