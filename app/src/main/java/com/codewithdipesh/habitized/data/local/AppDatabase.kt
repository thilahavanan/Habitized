package com.codewithdipesh.habitized.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codewithdipesh.habitized.DATABASE_VERSION
import com.codewithdipesh.habitized.data.local.converter.Converters
import com.codewithdipesh.habitized.data.local.dao.GoalDao
import com.codewithdipesh.habitized.data.local.dao.HabitDao
import com.codewithdipesh.habitized.data.local.dao.HabitProgressDao
import com.codewithdipesh.habitized.data.local.dao.ImageProgressDao
import com.codewithdipesh.habitized.data.local.dao.OneTimeTaskDao
import com.codewithdipesh.habitized.data.local.dao.SubTaskDao
import com.codewithdipesh.habitized.data.local.entity.GoalEntity
import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.data.local.entity.HabitProgressEntity
import com.codewithdipesh.habitized.data.local.entity.ImageProgressEntity
import com.codewithdipesh.habitized.data.local.entity.OneTimeTaskEntity
import com.codewithdipesh.habitized.data.local.entity.SubtaskEntity

@Database(
    entities = [
        OneTimeTaskEntity::class,
        GoalEntity::class,
        HabitEntity::class,
        HabitProgressEntity::class,
        SubtaskEntity::class,
        ImageProgressEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oneTimeTaskDao(): OneTimeTaskDao
    abstract fun goalDao(): GoalDao
    abstract fun habitDao(): HabitDao
    abstract fun habitProgressDao(): HabitProgressDao
    abstract fun subtaskDao(): SubTaskDao
    abstract fun imageProgressDao(): ImageProgressDao

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
                    .addMigrations(MIGRATION_10_11)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            //Enable foreign key constarints
                            db.execSQL("PRAGMA foreign_keys=ON")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE habits ADD COLUMN reminderType TEXT")
        db.execSQL("""
             UPDATE habits
             SET reminderType = 'Once'
             WHERE reminder_time IS NOT NULL
        """.trimIndent())
        db.execSQL("ALTER TABLE habits ADD COLUMN reminderFrom TEXT")
        db.execSQL("ALTER TABLE habits ADD COLUMN reminderTo TEXT")
        db.execSQL("ALTER TABLE habits ADD COLUMN reminderInterval INTEGER")
    }
}
