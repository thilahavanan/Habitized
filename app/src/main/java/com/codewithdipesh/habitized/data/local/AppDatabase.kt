package com.codewithdipesh.habitized.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codewithdipesh.habitized.data.local.dao.GoalDao
import com.codewithdipesh.habitized.data.local.dao.HabitDao
import com.codewithdipesh.habitized.data.local.dao.HabitProgressDao
import com.codewithdipesh.habitized.data.local.dao.OneTimeTaskDao
import com.codewithdipesh.habitized.data.local.dao.SubTaskDao
import com.codewithdipesh.habitized.data.local.entity.Goal
import com.codewithdipesh.habitized.data.local.entity.Habit
import com.codewithdipesh.habitized.data.local.entity.HabitProgress
import com.codewithdipesh.habitized.data.local.entity.OneTimeTaskEntity
import com.codewithdipesh.habitized.data.local.entity.SubtaskEntity

@Database(
    entities = [
        OneTimeTaskEntity::class,
        Goal::class,
        Habit::class,
        HabitProgress::class,
        SubtaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oneTimeTaskDao(): OneTimeTaskDao
    abstract fun goalDao(): GoalDao
    abstract fun habitDao(): HabitDao
    abstract fun habitProgressDao(): HabitProgressDao
    abstract fun subtaskDao(): SubTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habit_tracker_db"
                )
                    .fallbackToDestructiveMigration()  // Handles DB version upgrades
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
