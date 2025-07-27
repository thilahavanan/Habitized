package com.codewithdipesh.habitized.widget.data

import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.widget.WidgetType
import java.time.LocalDate
import java.util.UUID

data class HabitWidgetInfo(
    val habitId: UUID,
    val name: String,
    val colorKey: String,
    val frequency: WidgetType,
    val daysOfWeek: List<Int> = emptyList(),
    val daysOfMonth: List<Int>? = null,
    val currentStreak: Int = 0,
    val progress: List<ProgressWidgetData> = emptyList(),
)

data class ProgressWidgetData(
    val date: LocalDate,
    val status: WidgetStatus
)

enum class WidgetStatus { Done, Skipped,Absent }

fun HabitToWidgetStatus(status : Status) : WidgetStatus {
    return when(status){
        Status.Done -> WidgetStatus.Done
        Status.NotStarted -> WidgetStatus.Skipped
        Status.Cancelled -> WidgetStatus.Skipped
        Status.Ongoing -> WidgetStatus.Absent
    }
}