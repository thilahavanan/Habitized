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
    val title :String,
    val date: LocalDate,
    val currentCount: Int? = null,  // For habits like water intake
    val targetCount: Int? = null,  // For habits like water intake
    val durationValue: Float? = null,  // For time-based habits (e.g., sleep)
    val percentageValue: Float? = null,  // For stats like sleep quality
    val numberSessionDone: Int? = null,  // For session-based habits like Pomodoro
    val status: Status = Status.NotStarted,
    val notes: String? = null,
    val excuse: String? = null,
    val subtasks : List<SubTask>? = null,
    val fallAsleepTime: LocalTime? = null,  // Store as "HH:MM"
    val wakeUpTime: LocalTime? = null  // Store as "HH:MM"
)