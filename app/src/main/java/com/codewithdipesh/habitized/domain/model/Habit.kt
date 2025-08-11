package com.codewithdipesh.habitized.domain.model

import android.graphics.drawable.Icon
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.UUID

data class Habit(
    val habit_id: UUID? = UUID.randomUUID(),
    val title: String,
    val description: String?= null,
    val type: HabitType = HabitType.Count,
    val goal_id: UUID? = null,
    val start_date: LocalDate,
    val frequency: Frequency = Frequency.Daily, // (Daily, Weekly, Monthly, Custom)
    val days_of_week: List<Int> = mutableListOf(0,0,0,0,0,0,0), //"1,0,0,1,0,1,1"
    val daysOfMonth: List<Int>? = null, //"1,15,30"
    val reminderType: ReminderType? = ReminderType.Once(),
    val is_active: Boolean,
    val colorKey : String,
    val countParam : CountParam? = null,
    val countTarget:Int?,
    val duration: LocalTime?,
    val currentStreak :Int = 0,
    val maxStreak :Int = 0
)
