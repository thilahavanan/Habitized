package com.codewithdipesh.habitized.di

import android.content.Context
import com.codewithdipesh.habitized.data.alarmManager.AndroidAlarmScheduler
import com.codewithdipesh.habitized.data.local.AppDatabase
import com.codewithdipesh.habitized.data.local.dao.GoalDao
import com.codewithdipesh.habitized.data.local.dao.HabitDao
import com.codewithdipesh.habitized.data.local.dao.HabitProgressDao
import com.codewithdipesh.habitized.data.local.dao.ImageProgressDao
import com.codewithdipesh.habitized.data.local.dao.OneTimeTaskDao
import com.codewithdipesh.habitized.data.local.dao.SubTaskDao
import com.codewithdipesh.habitized.data.repository.HabitRepoImpl
import com.codewithdipesh.habitized.data.sharedPref.HabitPreference
import com.codewithdipesh.habitized.domain.alarmManager.AlarmScheduler
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideOneTimeTaskDao(db: AppDatabase): OneTimeTaskDao = db.oneTimeTaskDao()

    @Provides
    @Singleton
    fun provideGoalDao(db: AppDatabase): GoalDao = db.goalDao()

    @Provides
    @Singleton
    fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao()

    @Provides
    @Singleton
    fun provideHabitProgressDao(db: AppDatabase): HabitProgressDao = db.habitProgressDao()

    @Provides
    @Singleton
    fun provideSubtaskDao(db: AppDatabase): SubTaskDao = db.subtaskDao()

    @Provides
    @Singleton
    fun provideImageProgressDao(db: AppDatabase): ImageProgressDao = db.imageProgressDao()

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: HabitDao,
        habitProgressDao: HabitProgressDao,
        oneTimeTaskDao: OneTimeTaskDao,
        subtaskDao: SubTaskDao,
        goalDao: GoalDao,
        imageProgressDao: ImageProgressDao
    ): HabitRepository {
        return HabitRepoImpl(
            habitDao = habitDao,
            habitProgressDao = habitProgressDao,
            oneTimeTaskDao = oneTimeTaskDao,
            subtaskDao = subtaskDao,
            goalDao = goalDao,
            imageProgressDao = imageProgressDao

        )
    }

    @Provides
    @Singleton
    fun provideHabitPreference(@ApplicationContext context: Context): HabitPreference {
        return HabitPreference(context)
    }
    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AndroidAlarmScheduler(context)
    }

    @Provides fun provideHabitWidgetRepository(
        habitDao: HabitDao,
        progressDao: HabitProgressDao
    ): HabitWidgetRepository =
        HabitWidgetRepository(habitDao, progressDao)

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetRepositoryEntryPoint {
    fun habitWidgetRepository(): HabitWidgetRepository
}
