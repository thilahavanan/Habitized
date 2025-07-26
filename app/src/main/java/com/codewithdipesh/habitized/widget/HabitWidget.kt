package com.codewithdipesh.habitized.widget

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.codewithdipesh.habitized.widget.data.HabitWidgetDataStore
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import com.codewithdipesh.habitized.widget.elements.OverAllHabitWidgetContent

class WeeklyHabitWidget(
    private val repository: HabitWidgetRepository
) : GlanceAppWidget() {

    companion object {
        private val SMALL_SQUARE = DpSize(210.dp, 98.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(390.dp, 130.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(SMALL_SQUARE, HORIZONTAL_RECTANGLE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val habitId = HabitWidgetDataStore.getHabitIdForWidget(context, appWidgetId)

        if (habitId == null) {
            provideContent {
                //todo habit not available
            }
            return
        }

        val habitInfo = repository.getHabit(habitId, WidgetType.Weekly)

        provideContent {
//            WeeklyHabitWidgetContent(habitInfo)
        }
    }
}

class MonthlyHabitWidget(
    private val repository: HabitWidgetRepository
) : GlanceAppWidget() {

    companion object {
        private val SMALL_SQUARE = DpSize(210.dp, 130.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(440.dp, 250.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(SMALL_SQUARE, HORIZONTAL_RECTANGLE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val habitId = HabitWidgetDataStore.getHabitIdForWidget(context, appWidgetId)

        if (habitId == null) {
            provideContent {
                Text("Fuckked up")
            }
            return
        }

        val habitInfo = repository.getHabit(habitId, WidgetType.Overall)

        provideContent {
            OverAllHabitWidgetContent(habitInfo)
        }
    }
}
