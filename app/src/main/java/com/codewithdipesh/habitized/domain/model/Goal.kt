package com.codewithdipesh.habitized.domain.model

import java.util.Date
import java.util.UUID

data class Goal(
    val id:UUID,
    val title: String,
    val description: String? = null,
    val target_date: Date? = null,
    val progress: Int? = null,
    val habits: List<Habit> = emptyList()
)
