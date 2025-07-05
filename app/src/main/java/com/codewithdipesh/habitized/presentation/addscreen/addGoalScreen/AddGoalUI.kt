package com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen

import com.codewithdipesh.habitized.domain.model.Habit
import java.time.LocalDate
import java.util.UUID

data class AddGoalUI(
    val goal_id: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val target_date: LocalDate? = null,
    val start_date: LocalDate = LocalDate.now(),
    val isTargetDateVisible : Boolean = false,
    //so if we remove any habit have to update from habit also
    //so for tracking old habits (first tim fetching)
    val prevHabits : List<Habit> = emptyList(),
    val habits : List<Habit> = emptyList(),
    val availableHabits : List<Habit> = emptyList()
)
