package com.codewithdipesh.habitized.domain.repository
import com.codewithdipesh.habitized.domain.model.DayWiseTodo
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import com.codewithdipesh.habitized.domain.model.SubTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

interface HabitRepository {

    // Habits
    suspend fun addHabits(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: UUID)
    fun getHabitById(habitId: UUID): Flow<Habit?>

    // Habit Progress
    suspend fun addHabitProgress(progress: HabitProgress)
    suspend fun updateHabitProgress(progress: HabitProgress)
    fun getHabitProgress(habitId: UUID, date: String): Flow<HabitProgress?>
    fun getAllHabitProgress(habitId: UUID): Flow<List<HabitProgress>>

    // Goals
    suspend fun addGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goalId: UUID)
    suspend fun getGoalById(goalId: UUID): Flow<Goal?> //sending habits also with it
    fun getAllGoals(): Flow<List<Goal>>

    // One-Time Tasks
    suspend fun addOneTimeTask(task: OneTimeTask)
    suspend fun updateOneTimeTask(task: OneTimeTask)
    suspend fun deleteOneTimeTask(taskId: UUID)


    // Subtasks
    suspend fun insertSubtask(subtask: SubTask)
    suspend fun updateSubtask(subtask: SubTask)
    suspend fun deleteSubtask(subtaskId: UUID)
    suspend fun markSubTask(subtaskId: UUID,status:Boolean)

    fun getTotalTasks(date: LocalDate): DayWiseTodo
}
