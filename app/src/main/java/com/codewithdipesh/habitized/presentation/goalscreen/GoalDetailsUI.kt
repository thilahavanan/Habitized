package com.codewithdipesh.habitized.presentation.goalscreen

import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.presentation.goalscreen.components.GraphType
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class GoalDetailsUI(
    val id : UUID? = null,
    val title : String = "",
    val description : String = "",
    val targetDate : LocalDate ? = null,
    val startDate : LocalDate = LocalDate.now(),

    val showedGraphType : GraphType = GraphType.last_week,
    val effortList : List<Effort> = listOf(),
    val showedEfforts : List<Effort> = listOf(),

    val habits : List<Habit> = emptyList(),
    val onTrack : List<Habit> = emptyList(),
    val offTrack : List<Habit> = emptyList(),
    val AtRisk : List<Habit> = emptyList(),
    val closed : List<Habit> = emptyList()

)
data class Effort(val day: LocalDate, val effortLevel: Float)

