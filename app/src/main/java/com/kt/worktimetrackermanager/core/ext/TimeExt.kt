package com.kt.worktimetrackermanager.core.ext

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val formatterMinute = DateTimeFormatter.ofPattern("HH:mm")
private val formatterSecond = DateTimeFormatter.ofPattern("HH:mm:ss")

fun LocalDateTime.parseMinute(): String {
    return this.format(formatterMinute)
}

fun LocalDateTime.parseDate(): String {
    return this.format(formatter)
}

fun LocalTime.parse(): String {
    return this.format(formatterSecond)
}

fun LocalDate.parse(): String {
    return this.format(dateFormatter)
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}