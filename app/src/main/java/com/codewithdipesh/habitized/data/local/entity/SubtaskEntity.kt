package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "sub_tasks",
    foreignKeys = [
        ForeignKey(
            entity = HabitProgressEntity::class,
            parentColumns = ["progressId"],
            childColumns = ["habitProgressId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubtaskEntity(
    @PrimaryKey val subtaskId: UUID=UUID.randomUUID(),
    val title :String,
    val isCompleted : Boolean = false,
    val habitProgressId : UUID
)
