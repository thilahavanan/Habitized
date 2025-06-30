package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "imageProgress")
data class ImageProgressEntity(
    @PrimaryKey  val id : UUID,
    val habitId: UUID,
    val description: String,
    val date: LocalDate,
    val imagePath: String
)
