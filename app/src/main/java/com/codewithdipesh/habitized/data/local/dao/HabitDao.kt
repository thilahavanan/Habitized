package com.codewithdipesh.habitized.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits WHERE habit_id = :habitId")
    suspend fun getHabitById(habitId: UUID): HabitEntity?

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("DELETE FROM habits WHERE habit_id = :habitId")
    suspend fun deleteHabit(habitId: UUID)

    @Query("SELECT * FROM habits WHERE start_date >= :date")
    suspend fun getHabitsForTheDay(date: LocalDate) : List<HabitEntity>

    @Query("""
        UPDATE habits 
        SET is_active = :isActive 
        WHERE habit_id = :habitId
    """)
    suspend fun updateHabitStatus(habitId: UUID, isActive: Boolean)

    @Query("SELECT * FROM habits WHERE goal_id = :goalId")
    suspend fun getHabitsByGoal(goalId: UUID): Flow<List<HabitEntity>>
}
