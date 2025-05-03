package com.codewithdipesh.habitized.data.local.mapper

import com.codewithdipesh.habitized.data.local.entity.OneTimeTaskEntity
import com.codewithdipesh.habitized.data.local.entity.SubtaskEntity
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import com.codewithdipesh.habitized.domain.model.SubTask

fun OneTimeTask.toEntity() : OneTimeTaskEntity {
    return OneTimeTaskEntity(
        taskId = taskId,
        title = title,
        isCompleted = isCompleted,
        date = date,
        reminder_time = reminder_time
    )
}

fun OneTimeTaskEntity.toOneTimeTask() : OneTimeTask {
   return OneTimeTask(
       taskId = taskId,
       title = title,
       isCompleted = isCompleted,
       date = date,
       reminder_time = reminder_time,
   )
}