package com.codewithdipesh.habitized.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.ui.theme.HabitizedTheme
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import com.codewithdipesh.habitized.widget.data.HabitWidgetDataStore
import com.codewithdipesh.habitized.widget.data.HabitWidgetRepository
import com.kizitonwose.calendar.compose.WeekCalendar
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

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
            HabitizedTheme {
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

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Select Habit",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = playfair,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier
                            .padding(top = 30.dp)
                    )
                }
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp)
            ) {
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
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                onClick = { selectHabit(habit) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = habit.title,
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontFamily = regular,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                    Text(
                                        text = "Current Streak: ${habit.currentStreak}",
                                        style = TextStyle(
                                            color = Color.Gray,
                                            fontFamily = regular,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }
                        if(habits.isEmpty()){
                            item{
                                Text(
                                    text = "Pls Add a Habit First",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontFamily = regular,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
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
            try {
                val habitUUID = habit.habit_id!! // Save the habit ID
                HabitWidgetDataStore.saveHabitIdForWidget(
                    context = this@HabitWidgetConfigActivity,
                    widgetId = appWidgetId,
                    habitId = habitUUID
                )
                val resultValue = Intent().apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                }
                setResult(Activity.RESULT_OK, resultValue)
                finish()

            } catch (e: Exception) {
                Log.e("WidgetConfig", "Error saving habit: ${e.message}")
            }
        }
    }

}
