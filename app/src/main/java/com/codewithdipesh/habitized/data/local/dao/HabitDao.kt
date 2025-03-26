package com.codewithdipesh.habitized.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithdipesh.habitized.data.local.entity.Habit
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE habit_id = :habitId")
    suspend fun getHabitById(habitId: UUID): Habit?

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("DELETE FROM habits WHERE habit_id = :habitId")
    suspend fun deleteHabit(habitId: UUID)

    @Query("""
        UPDATE habits 
        SET is_active = :isActive 
        WHERE habit_id = :habitId
    """)
    suspend fun updateHabitStatus(habitId: UUID, isActive: Boolean)

    @Query("SELECT * FROM habits WHERE goal_id = :goalId")
    suspend fun getHabitsByGoal(goalId: UUID): Flow<List<Habit>>
}
