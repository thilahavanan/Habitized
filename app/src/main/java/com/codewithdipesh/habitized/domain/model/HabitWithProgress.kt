package com.codewithdipesh.habitized.domain.model

import java.time.LocalDate

data class HabitWithProgress(
    val habit : Habit,
    val date : LocalDate,
    val progress : HabitProgress,
    val subtasks : List<SubTask> = emptyList()
)
