package com.codewithdipesh.habitized.data.repository

import com.codewithdipesh.habitized.data.local.dao.GoalDao
import com.codewithdipesh.habitized.data.local.dao.HabitDao
import com.codewithdipesh.habitized.data.local.dao.HabitProgressDao
import com.codewithdipesh.habitized.data.local.dao.OneTimeTaskDao
import com.codewithdipesh.habitized.data.local.dao.SubTaskDao
import com.codewithdipesh.habitized.domain.model.DayWiseTodo
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

class HabitRepoImpl(
    private val habitDao: HabitDao,
    private val habitProgressDao: HabitProgressDao,
    private val oneTimeTaskDao: OneTimeTaskDao,
    private val subtaskDao: SubTaskDao,
    private val goalDao: GoalDao
) : HabitRepository {
    override suspend fun addHabits(habit: Habit) {
        TODO("Not yet implemented")
    }
    override suspend fun updateHabit(habit: Habit) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteHabit(habitId: UUID) {
        TODO("Not yet implemented")
    }
    override fun getHabitById(habitId: UUID): Flow<Habit?> {
        TODO("Not yet implemented")
    }



    override suspend fun addHabitProgress(progress: HabitProgress) {
        TODO("Not yet implemented")
    }
    override suspend fun updateHabitProgress(progress: HabitProgress) {
        TODO("Not yet implemented")
    }
    override fun getHabitProgress(
        habitId: UUID,
        date: String
    ): Flow<HabitProgress?> {
        TODO("Not yet implemented")
    }
    override fun getAllHabitProgress(habitId: UUID): Flow<List<HabitProgress>> {
        TODO("Not yet implemented")
    }



    override suspend fun addGoal(goal: Goal) {
        TODO("Not yet implemented")
    }
    override suspend fun updateGoal(goal: Goal) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteGoal(goalId: UUID) {
        TODO("Not yet implemented")
    }
    override suspend fun getGoalById(goalId: UUID): Flow<Goal?> {
        TODO("Not yet implemented")
    }
    override fun getAllGoals(): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }



    override suspend fun addOneTimeTask(task: OneTimeTask) {
        TODO("Not yet implemented")
    }
    override suspend fun updateOneTimeTask(task: OneTimeTask) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteOneTimeTask(taskId: UUID) {
        TODO("Not yet implemented")
    }



    override suspend fun insertSubtask(subtask: SubTask) {
        TODO("Not yet implemented")
    }
    override suspend fun updateSubtask(subtask: SubTask) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteSubtask(subtaskId: UUID) {
        TODO("Not yet implemented")
    }
    override suspend fun markSubTask(subtaskId: UUID, status: Boolean) {
        TODO("Not yet implemented")
    }


    override fun getTotalTasks(date: LocalDate): DayWiseTodo {
        TODO("Not yet implemented")
    }
}