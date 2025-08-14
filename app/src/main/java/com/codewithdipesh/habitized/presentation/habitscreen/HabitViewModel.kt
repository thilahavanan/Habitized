package com.codewithdipesh.habitized.presentation.habitscreen

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.alarmManager.AlarmItem
import com.codewithdipesh.habitized.domain.alarmManager.AlarmScheduler
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.domain.util.getNextAlarmDateTime
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.WeekDayMapToInt
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.String

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repo : HabitRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {

    private val _state = MutableStateFlow(HabitDetailsUI())
    val state = _state.asStateFlow()

    private val _shareCardState = MutableStateFlow(ShareCardUI())
    val shareCardState = _shareCardState.asStateFlow()

    suspend fun init( id : UUID){
        val habit = repo.getHabitById(id)
        _state.value = _state.value.copy(
            id = id,
            title = habit!!.title,
            description = habit.description,
            type = habit.type,
            frequency = habit.frequency,
            targetCount = habit.countTarget ?: 0,
            targetTime = habit.duration,
            countParam = habit.countParam,
            days_of_week = habit.days_of_week,
            daysOfMonth = habit.daysOfMonth ?: emptyList(),
            reminderType = habit.reminderType,
            currentStreak = habit.currentStreak,
            maximumStreak = habit.maxStreak
        )
        val completed  = repo.getAllCompletedProgress(id)
        val total = when(state.value.frequency){
            Frequency.Daily -> {
                (LocalDate.now().toEpochDay() - habit.start_date.toEpochDay()).toInt() + 1
            }
            Frequency.Weekly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val totalWeeks = (start.until(now).days / 7) + 1
                // Count only the scheduled days of the week
                totalWeeks * habit.days_of_week.count { it == 1 }
            }
            Frequency.Monthly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val months = (now.year - start.year) * 12 + (now.monthValue - start.monthValue) + 1
                months * (habit.daysOfMonth?.size ?: 0)
            }
            else -> 0
        }
        _state.value = _state.value.copy(
            totalCompleted = completed.size,
            completionRate = (completed.size.toFloat() / total.toFloat() * 100).toInt(),
            progressList = completed
        )
        val imageProgresses = repo.getImageProgressesForHabit(id)
        _state.value = _state.value.copy(
            imageProgresses = imageProgresses
        )
        initShareCard(
            title = habit.title,
            frequency = habit.frequency,
            streak = habit.currentStreak,
            colorKey = habit.colorKey,
            progressList = completed,
            startDate = habit.start_date,
            daysOfWeek = habit.days_of_week,
            daysOfMonth = habit.daysOfMonth ?: emptyList()
        )
    }

    suspend fun saveImage(id : UUID?,image : String, date : LocalDate, description : String){
        repo.insertImageProgress(
            ImageProgress(
                id = id ?: UUID.randomUUID(),
                habitId = state.value.id!!,
                imagePath = image,
                date = date,
                description = description
            )
        )
    }

    suspend fun deleteImage(id :UUID){
        repo.deleteImageProgress(id)
    }
    suspend fun deleteHabit(id: UUID){
        repo.deleteHabit(id)
        //canceling the alarm if present
        if(_state.value.reminderType != null){
            val alarmItem = AlarmItem(
                id = id,
                //only id is needed as request code is this
                //other details are dummy
                nextAlarmDateTime = LocalDateTime.now(),
                reminderType = _state.value.reminderType!!,
                title = "",
                text = "",
                frequency = Frequency.Daily,
                daysOfWeek = emptyList(),
                daysOfMonth = emptyList()
            )
            alarmScheduler.cancel(alarmItem)
        }
    }

    fun initShareCard(
        title: String,
        streak:Int,
        startDate : LocalDate,
        frequency : Frequency,
        colorKey: String,
        daysOfWeek: List<Int>,
        daysOfMonth: List<Int>,
        progressList: List<HabitProgress>
    ){
        val (weeklyRange, OverallRange) = getWeeklyandOverAllDates()
        _shareCardState.value = _shareCardState.value.copy(
            title = title,
            currentStreak = streak,
            frequency = frequency,
            colorKey = colorKey,
            progressList = progressList,
            startDate = startDate,
            daysOfWeek = daysOfWeek,
            daysOfMonth = daysOfMonth,
            WeeklyDateRange = weeklyRange,
            OverAllDateRange = OverallRange
        )
    }
    fun getWeeklyandOverAllDates() : Pair< List<LocalDate>,List<LocalDate> > {
        val startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY)
        val weeklyRange = (0..6).map { offset ->
            startOfWeek.plusDays(offset.toLong())
        }
        val monthlyRange = (0..(119 + LocalDate.now().dayOfWeek.value) )
            .map{ offset ->
                LocalDate.now().minusDays(offset.toLong())
            }
            .reversed()

        return weeklyRange to monthlyRange
    }
    fun setBackground(bg: Background){
        _shareCardState.value = _shareCardState.value.copy(
            selectedBackground = bg
        )
    }
    fun setImage(image: Bitmap){
        _shareCardState.value = _shareCardState.value.copy(
            imageBgBitmap = image
        )
    }
    fun shareCard(context: Context, bitmap: Bitmap){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my progress")
        shareIntent.type = "image/png"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri = getImageUri(context, bitmap)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(shareIntent, "Share Progress"))
    }
    fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val imagesFolder = File(context.cacheDir, "shared_images")
        imagesFolder.mkdirs()
        val timeMillis = System.currentTimeMillis()
        val fileName = "Habit_Progress_${timeMillis}.png"
        val file = File(imagesFolder, fileName)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
    fun downloadCard(context: Context, bitmap: Bitmap): Boolean {
        return try {
            val fileName = "Habit_Progress_${System.currentTimeMillis()}.png"
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(picturesDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            // Notify gallery about new image
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf("image/png"),
                null
            )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




    fun clearUi(){
        _state.value = HabitDetailsUI()
    }
}