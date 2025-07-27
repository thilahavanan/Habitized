package com.codewithdipesh.habitized.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class WeeklyHabitWidgetProvider : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WeeklyHabitWidget()
}
@AndroidEntryPoint
class MonthlyHabitWidgetProvider : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = MonthlyHabitWidget()
}



enum class WidgetType { Weekly, Overall }
