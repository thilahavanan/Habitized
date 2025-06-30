package com.codewithdipesh.habitized.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.util.UUID

data class ImageProgress(
    val id : UUID = UUID.randomUUID(),
    val habitId: String,
    val description: String,
    val date: LocalDate,
    val imagePath: String
)