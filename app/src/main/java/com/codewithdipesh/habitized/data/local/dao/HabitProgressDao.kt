package com.codewithdipesh.habitized.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithdipesh.habitized.data.local.entity.HabitProgress
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface HabitProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: HabitProgress)

    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId")
    fun getHabitProgress(habitId: UUID): Flow<List<HabitProgress>>

    @Query("DELETE FROM habit_progress WHERE progressId = :progressId")
    suspend fun deleteProgress(progressId: UUID)

    @Query("SELECT * FROM habit_progress WHERE progressId = :habitProgressId")
    suspend fun getHabitProgressById(habitProgressId: UUID): HabitProgress

    @Query("SELECT * FROM habit_progress WHERE date = :date")
    fun getAllProgress(date: LocalDate): Flow<List<HabitProgress>>

    @Query("""
        UPDATE habit_progress 
        SET currentCount = :currentCount, 
            durationValue = :durationValue, 
            percentageValue = :percentageValue 
        WHERE progressId = :progressId
    """)
    suspend fun updateProgress(
        progressId: UUID,
        currentCount: Int?,
        durationValue: Float?,
        percentageValue: Float?
    )
}
