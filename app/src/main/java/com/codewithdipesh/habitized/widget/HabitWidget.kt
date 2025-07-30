package com.codewithdipesh.habitized.widget

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.codewithdipesh.habitized.di.WidgetRepositoryEntryPoint
import com.codewithdipesh.habitized.widget.data.HabitWidgetDataStore
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import com.codewithdipesh.habitized.widget.elements.OverAllHabitWidgetContent
import com.codewithdipesh.habitized.widget.elements.WeeklyHabitWidgetContent
import dagger.hilt.android.EntryPointAccessors

class WeeklyHabitWidget (): GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val habitId = HabitWidgetDataStore.getHabitIdForWidget(context, appWidgetId)

        if (habitId == null) {
            provideContent {
                Text("Issue loading Habit")
            }
            return
        }

        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetRepositoryEntryPoint::class.java
        )
        val repository = entryPoint.habitWidgetRepository()
        val habitInfo = repository.getHabit(habitId, WidgetType.Weekly)

        provideContent {
            WeeklyHabitWidgetContent(habitInfo)
        }
    }
}


class MonthlyHabitWidget() : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val habitId = HabitWidgetDataStore.getHabitIdForWidget(context, appWidgetId)

        if (habitId == null) {
            provideContent {
                Text("Issue loading Habit")
            }
            return
        }

        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetRepositoryEntryPoint::class.java
        )
        val repository = entryPoint.habitWidgetRepository()
        val habitInfo = repository.getHabit(habitId, WidgetType.Overall)

        provideContent {
            OverAllHabitWidgetContent(habitInfo)
        }
    }
}
