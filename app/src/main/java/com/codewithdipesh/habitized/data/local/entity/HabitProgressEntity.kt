package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithdipesh.habitized.domain.model.Status
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity(tableName = "habit_progress")
data class HabitProgressEntity(
    @PrimaryKey val progressId: UUID = UUID.randomUUID(),
    val habitId: UUID,
    val title :String,
    val date: LocalDate,  // Store as "YYYY-MM-DD"
    val currentCount: Int? = null,  // For habits like water intake
    val targetCount: Int? = null,  // For habits like water intake
    val durationValue: Float? = null,  // For time-based habits (e.g., sleep)
    val percentageValue: Float? = null,  // For stats like sleep quality
    val numberSessionDone: Int? = null,  // For session-based habits like Pomodoro
    val status: String = Status.NotStarted.toString(),
    val notes: String? = null,
    val excuse: String? = null,
    val fallAsleepTime: LocalTime? = null,  // Store as "HH:MM"
    val wakeUpTime: LocalTime? = null  // Store as "HH:MM"
)