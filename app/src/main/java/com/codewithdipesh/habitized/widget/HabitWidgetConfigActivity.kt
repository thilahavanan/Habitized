package com.codewithdipesh.habitized.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.widget.data.HabitWidgetDataStore
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitWidgetConfigActivity : ComponentActivity() {

    @Inject
    lateinit var repository: HabitWidgetRepository

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get widget ID from intent
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID


        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            MaterialTheme {
                HabitSelectionScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HabitSelectionScreen() {
        var habits by remember { mutableStateOf<List<Habit>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            habits = repository.getAllHabits()
            isLoading = false
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Habit for Widget",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(habits) { habit ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { selectHabit(habit) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Current Streak: ${habit.currentStreak}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // SAVE is called here when user selects habit
    private fun selectHabit(habit: Habit) {
        lifecycleScope.launch {
            //save
            HabitWidgetDataStore.saveHabitIdForWidget(
                context = this@HabitWidgetConfigActivity,
                widgetId = appWidgetId,
                habitId = habit.habit_id!!
            )
            //force update to avoid ( fetching before saving and null error)
            MonthlyHabitWidget(repository).updateAll(this@HabitWidgetConfigActivity)

            val resultValue = Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }
}
