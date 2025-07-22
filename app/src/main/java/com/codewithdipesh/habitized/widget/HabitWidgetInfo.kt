package com.codewithdipesh.habitized.widget

import java.time.LocalDate
import java.util.UUID

data class HabitWidgetInfo(
    val habitId: UUID,
    val name: String,
    val colorKey: String,
    val frequency: WidgetFrequency,
    val daysOfWeek: List<Int> = emptyList(),
    val daysOfMonth: List<Int>? = null,
    val currentStreak: Int = 0
)

data class ProgressWidgetData(
    val date: LocalDate,
    val status: WidgetStatus
)

enum class WidgetFrequency { Daily, Weekly, Monthly }
enum class WidgetStatus { Done, Skipped,Absent }