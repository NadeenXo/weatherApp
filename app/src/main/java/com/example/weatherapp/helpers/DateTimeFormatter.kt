package com.example.weatherapp.helpers

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class MyDateTimeFormatter {
    companion object {
        private val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
        private lateinit var localDateTime: LocalDateTime

        fun formatDateTime(dateTime: String): String {
            val epochTime = dateTime.toLong()
            localDateTime = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC)
                .plusHours(2)
            val dayOfWeek = localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            val month = localDateTime.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            val dayOfMonth = localDateTime.dayOfMonth
            val time = localDateTime.format(DateTimeFormatter.ofPattern("h:mm a"))

            return "$dayOfWeek $month $dayOfMonth | $time"
        }

        fun formatDateTimeToHours(dateTime: String): String? {
            localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter)
            return localDateTime.format(DateTimeFormatter.ofPattern("h a"))
        }

        fun formatDateTimeToTime(dateTime: String): String {
            val epochTime = dateTime.toLong()
            localDateTime = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.UTC)
                .plusHours(2)
            return localDateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        }

        fun formatDateTimeToDay(dateTime: String): String? {
            localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter)
            return localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        }

        fun getLocalDateTime(dateTime: String): LocalDateTime {
            return LocalDateTime.parse(dateTime, dateTimeFormatter)
        }

        fun formatLocalDate(date: String): String? {
            val localDate = LocalDate.parse(date, dateFormatter)
            return localDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
        }

        fun getLocalDate(date: String): LocalDate {
            return LocalDate.parse(date, dateFormatter)
        }
    }
}
