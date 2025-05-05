package com.codewithdipesh.habitized.domain.model

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.Color
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.UUID

data class HabitProgress(
    val progressId: UUID,
    val habitId: UUID,
    val date: LocalDate,
    val type: HabitType = HabitType.Count,
    val countParam: CountParam,
    val currentCount: Int? = null, //count
    val targetCount: Int? = null,
    val currentDurationValue: Float? = null, //duration
    val targetDurationValue: Float? = null,
    val currentSessionNumber: Int? = null,  // For session-based habits like Pomodoro
    val targetSessionNumber: Int? = null,
    //2 session 25 minute -> targetSession = 2, currentSession = 0 ,targetDuration=25 minute
    val status: Status = Status.NotStarted,
    val notes: String? = null,
    val excuse: String? = null
)