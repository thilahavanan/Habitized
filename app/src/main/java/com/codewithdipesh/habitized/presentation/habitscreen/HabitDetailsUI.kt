package com.codewithdipesh.habitized.presentation.habitscreen

import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.model.ReminderType
import java.time.LocalTime
import java.util.UUID

data class HabitDetailsUI(
    val id : UUID? = null,
    val title : String = "",
    val description : String? = null,
    val type :HabitType = HabitType.OneTime,
    val frequency : Frequency = Frequency.Daily,
    val days_of_week : List<Int> = emptyList(),
    val daysOfMonth : List<Int> = emptyList(),
    val targetCount :Int? = 0,
    val countParam: CountParam? = null,
    val targetTime : LocalTime? = null,
    val reminderType : ReminderType? = null,
    val currentStreak :Int = 0,
    val maximumStreak :Int = 0,
    val totalCompleted :Int = 0,
    val completionRate :Int = 0,

    val progressList : List<HabitProgress> = emptyList(),
    val imageProgresses : List<ImageProgress> = emptyList()
)
