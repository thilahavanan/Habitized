package com.codewithdipesh.habitized.domain.model

import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class Goal(
    val id:UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val target_date: LocalDate? = null,
    val start_date: LocalDate? = LocalDate.now(),
    val progress: Int? = null,
    val habits: List<Habit> = emptyList()
)
