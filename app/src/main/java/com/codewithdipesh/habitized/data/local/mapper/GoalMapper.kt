package com.codewithdipesh.habitized.data.local.mapper

import com.codewithdipesh.habitized.data.local.entity.GoalEntity
import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitType

fun Goal.toEntity(): GoalEntity {
    return GoalEntity(
        goal_id=id,
        title = title,
        description = description,
        target_date = target_date,
        progress = progress,
    )
}

fun GoalEntity.toGoal(habits :List<Habit>) : Goal {
    return Goal(
        id=goal_id,
        title = title,
        description = description,
        target_date = target_date,
        progress = progress,
        habits = habits
    )
}