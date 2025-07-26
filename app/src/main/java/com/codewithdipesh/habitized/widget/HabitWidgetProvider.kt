package com.codewithdipesh.habitized.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class WeeklyHabitWidgetProvider : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var repository: HabitWidgetRepository

    override val glanceAppWidget: GlanceAppWidget
        get() = WeeklyHabitWidget(repository) // Pass injected repository
}

@AndroidEntryPoint
class MonthlyHabitWidgetProvider : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var repository: HabitWidgetRepository

    override val glanceAppWidget: GlanceAppWidget
        get() = MonthlyHabitWidget(repository) // Pass injected repository
}


enum class WidgetType { Weekly, Overall }
