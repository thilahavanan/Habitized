package com.codewithdipesh.habitized.domain.model

import android.util.Log
import java.time.LocalTime

sealed class ReminderType(val displayName: String) {
    data class Once(
        val reminderTime : LocalTime = LocalTime.now()
    ) : ReminderType("Once")
    data class Interval(
        val interval: Int = 120,
        val fromTime : LocalTime = LocalTime.now(),
        val toTime : LocalTime = LocalTime.now().plusMinutes(720)
    ): ReminderType("Interval")

    override fun toString(): String = displayName

    companion object {
        fun getTypes() : List<ReminderType>{
            return listOf(Once(), Interval( ))
        }
    }
}
