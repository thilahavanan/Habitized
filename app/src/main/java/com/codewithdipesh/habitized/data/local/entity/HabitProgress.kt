package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "habit_progress")
data class HabitProgress(
    @PrimaryKey val progressId: UUID = UUID.randomUUID(),
    val habitId: UUID,
    val title :String,
    val date: String,  // Store as "YYYY-MM-DD"
    val currentCount: Int? = null,  // For habits like water intake
    val targetCount: Int? = null,  // For habits like water intake
    val durationValue: Float? = null,  // For time-based habits (e.g., sleep)
    val percentageValue: Float? = null,  // For stats like sleep quality
    val numberSessionDone: Int? = null,  // For session-based habits like Pomodoro
    val status: String? = null,
    val notes: String? = null,
    val excuse: String? = null,
    val fallAsleepTime: String? = null,  // Store as "HH:MM"
    val wakeUpTime: String? = null  // Store as "HH:MM"
)