package com.codewithdipesh.habitized.data.local.mapper

import com.codewithdipesh.habitized.data.local.entity.GoalEntity
import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.data.local.entity.ImageProgressEntity
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress

fun ImageProgress.toEntity(): ImageProgressEntity {
    return ImageProgressEntity(
        id = id,
        habitId = habitId,
        description = description,
        date = date,
        imagePath = imagePath
    )
}

fun ImageProgressEntity.toImageProgress(): ImageProgress {
    return ImageProgress(
        id = id,
        habitId = habitId,
        description = description,
        date = date,
        imagePath = imagePath
    )
}