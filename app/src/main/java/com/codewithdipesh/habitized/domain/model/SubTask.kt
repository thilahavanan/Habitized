package com.codewithdipesh.habitized.domain.model

import java.util.UUID

data class SubTask(
    val subtaskId: UUID,
    val title :String,
    val isCompleted : Boolean = false,
    val habitProgressId : UUID
)
