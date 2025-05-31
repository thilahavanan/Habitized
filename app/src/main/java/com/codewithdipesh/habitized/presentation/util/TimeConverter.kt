package com.codewithdipesh.habitized.presentation.util

import java.time.LocalTime
import kotlin.math.min

fun LocalTime.toWord(): String {
    val hour = this.hour
    val minute = this.minute
    val second = this.second

    val parts = mutableListOf<String>()
    if (hour > 0) parts.add("${hour} Hr")
    if (minute > 0) parts.add("${minute} Min")
    if (second > 0) parts.add("${second} S")

    return parts.joinToString(" ")
}

fun LocalTime.SingleString():String{
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

fun String.toLocalTime() : LocalTime{
    val splited = split(":")
    require(splited.size == 3) { "Invalid time format, expected HH:mm:ss" }

    val hour = splited[0].toInt()
    val minute = splited[1].toInt()
    val seconds = splited[2].toInt()

    return LocalTime.of(hour,minute,seconds)
}