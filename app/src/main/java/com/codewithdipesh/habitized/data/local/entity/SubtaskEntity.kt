package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "sub_tasks")
data class SubtaskEntity(
    @PrimaryKey val subtaskId: UUID=UUID.randomUUID(),
    val title :String,
    val isCompleted : Boolean = false,
    val habitProgressId : UUID
)
