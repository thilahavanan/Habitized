package com.codewithdipesh.habitized.data.local

import android.database.Cursor
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate10To11_PreservesHabitData() {
        // 1. Create old DB schema (version 10)
        val db = helper.createDatabase(TEST_DB, 10)

        // 2. Insert a sample habit, emulating the old HabitEntity (no new columns!)
        val habitId = UUID.randomUUID().toString()
        val title = "Test Habit"
        val description = "Do something great"
        val type = HabitType.Count.toString()
        val startDate = LocalDate.of(2023, 1, 1).toString()
        val frequency = Frequency.Daily.toString()
        val daysOfWeek = "1,1,1,1,1,1,1"
        val daysOfMonth = "1,15,30"
        val reminderTime = LocalTime.of(8, 0).toString()
        val isActive = 1
        val colorKey = "red"
        val countParam = "Glasses"
        val countTarget = 8
        val duration = "01:00:00"
        val currentStreak = 1
        val maxStreak = 2

        db.execSQL("""
            INSERT INTO habits 
            (habit_id, title, description, type, goal_id, start_date, frequency, days_of_week, daysOfMonth, reminder_time, is_active, colorKey, countParam, countTarget, duration, currentStreak, maxStreak)
            VALUES (
                '$habitId',
                '$title',
                '$description',
                '$type',
                NULL,
                '$startDate',
                '$frequency',
                '$daysOfWeek',
                '$daysOfMonth',
                '$reminderTime',
                $isActive,
                '$colorKey',
                '$countParam',
                $countTarget,
                '$duration',
                $currentStreak,
                $maxStreak
            )
        """.trimIndent())

        db.close() // Forces DB to disk and migration will be triggered next

        // 3. Open DB with Room and migrate (triggers MIGRATION_10_11)
        val migratedDb = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(MIGRATION_10_11)
            .allowMainThreadQueries() // In tests only
            .build()

        // 4. Query: check habit row is still there and correct
        val cursor: Cursor = migratedDb.query("SELECT * FROM habits WHERE habit_id = ?", arrayOf(habitId))
        assert(cursor.count == 1)
        cursor.moveToFirst()
        val titleAfter = cursor.getString(cursor.getColumnIndexOrThrow("title"))
        val descriptionAfter = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        val startDateAfter = cursor.getString(cursor.getColumnIndexOrThrow("start_date"))
        val daysOfMonthAfter = cursor.getString(cursor.getColumnIndexOrThrow("daysOfMonth"))
        val maxStreakAfter = cursor.getInt(cursor.getColumnIndexOrThrow("maxStreak"))
        // Feel free to check more columns!

        assert(titleAfter == title)
        assert(descriptionAfter == description)
        assert(startDateAfter == startDate)
        assert(daysOfMonthAfter == daysOfMonth)
        assert(maxStreakAfter == maxStreak)

        cursor.close()
        migratedDb.close()
    }
}