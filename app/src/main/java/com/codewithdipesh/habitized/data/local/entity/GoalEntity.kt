package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val goal_id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val target_date: Date? = null,
    val progress: Int? = null
)
