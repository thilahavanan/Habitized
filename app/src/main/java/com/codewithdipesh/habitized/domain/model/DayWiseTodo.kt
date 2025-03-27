package com.codewithdipesh.habitized.domain.model

data class DayWiseTodo(
    val habitProgresses : List<HabitProgress> = emptyList(),
    val tasks : List<OneTimeTask> = emptyList(),
    val goals :List<Goal> = emptyList()
)
