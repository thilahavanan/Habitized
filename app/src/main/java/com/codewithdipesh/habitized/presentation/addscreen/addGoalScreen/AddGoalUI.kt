package com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen

import com.codewithdipesh.habitized.domain.model.Habit
import java.time.LocalDate
import java.util.UUID

data class AddGoalUI(
    val goal_id: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val target_date: LocalDate? = null,
    val isTargetDateVisible : Boolean = false,
    val habits : List<Habit> = emptyList(),
    val availableHabits : List<Habit> = emptyList()
)
