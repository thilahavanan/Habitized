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
    val closed : List<Habit> = emptyList(),

)
data class Effort(val day: LocalDate, val effortLevel: Float)


val dummyEffortList = listOf(
    Effort(LocalDate.now(),20f),
    Effort(LocalDate.now().minusDays(1),100f),
    Effort(LocalDate.now().minusDays(2),20f),
    Effort(LocalDate.now().minusDays(3),100f),
    Effort(LocalDate.now().minusDays(4),0f),
    Effort(LocalDate.now().minusDays(5),45f),
    Effort(LocalDate.now().minusDays(6),20f),
    Effort(LocalDate.now().minusDays(7),60f),
    Effort(LocalDate.now().minusDays(8),80f),
    Effort(LocalDate.now().minusDays(9),100f),
    Effort(LocalDate.now().minusDays(10),60f),
    Effort(LocalDate.now().minusDays(11),60f),
    Effort(LocalDate.now().minusDays(12),40f),
    Effort(LocalDate.now().minusDays(13),60f),
    Effort(LocalDate.now().minusDays(14),70f),
    Effort(LocalDate.now().minusDays(15),90f),
    Effort(LocalDate.now().minusDays(16),100f),
    Effort(LocalDate.now().minusDays(17),40f)
)