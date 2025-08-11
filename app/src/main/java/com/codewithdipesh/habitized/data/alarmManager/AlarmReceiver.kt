package com.codewithdipesh.habitized.data.alarmManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ALARM_NOTIFICATION_ID
import com.codewithdipesh.habitized.domain.alarmManager.AlarmItem
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.ReminderType
import com.codewithdipesh.habitized.domain.util.getNextAlarmDateTime
import com.codewithdipesh.habitized.presentation.util.Days
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.toDaysOfWeek
import com.codewithdipesh.habitized.presentation.util.toLocalTime
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //check first
        if(context == null || intent == null) return
        //extracting
        val id = intent.getStringExtra("id") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        val text =  intent.getStringExtra("text") ?: "Don't let the streak die"
        val frequency =  intent.getStringExtra("frequency") ?: ""
        val reminderTypeStr =  intent.getStringExtra("reminderType") ?: ""
        val reminderInterval =  intent.getIntExtra("reminderInterval",120)
        val reminderFrom =  intent.getStringExtra("reminderFrom") ?: ""
        val reminderTo =  intent.getStringExtra("reminderTo") ?: ""
        val reminderTime =  intent.getStringExtra("reminderTime") ?: ""
        val daysOfWeek =  intent.getIntArrayExtra("daysOfWeek")?.toList() ?: emptyList<Int>()
        val daysOfMonth =  intent.getIntArrayExtra("daysOfMonth")?.toList() ?:emptyList<Int>()

        if(title.isEmpty()) return
        //notification
        showNotification(context,title,text)

        val reminderType: ReminderType? = when (reminderTypeStr) {
            "Once" -> {
                val time = try { LocalTime.parse(reminderTime) } catch (e: Exception) { null }
                time?.let { ReminderType.Once(it) }
            }
            "Interval" -> {
                val interval = reminderInterval
                val from = try { LocalTime.parse(reminderFrom) } catch (e: Exception) { null }
                val to = try { LocalTime.parse(reminderTo) } catch (e: Exception) { null }
                if (interval > 0 && from != null && to != null) ReminderType.Interval(interval, from, to) else null
            }
            else -> null
        }
        //schedule next alarm
        if(frequency.isNotEmpty() && reminderType != null){
            scheduleNextAlarm(
                context,
                UUID.fromString(id),
                title,
                text,
                Frequency.fromString(frequency),
                LocalDateTime.now(),
                reminderType,
                daysOfWeek,
                daysOfMonth
            )
        }
    }

    private fun showNotification(context: Context,title : String ,text : String){
        val channelId = "ALARM_CHANNEL"
        val channelName = "Habit Alarm Notifications"

        //create channel
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        //intent to open the app when notification is clicked
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val resources = context.resources
        val notification = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_action_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_action_name))
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(ALARM_NOTIFICATION_ID,notification)
    }

    private fun scheduleNextAlarm(
        context: Context,
        id: UUID,
        title: String,
        text: String,
        frequency: Frequency,
        now: LocalDateTime,
        reminderType: ReminderType,
        daysOfWeek: List<Int>,
        daysOfMonth: List<Int>
    ) {
        val selectedDays = IntToWeekDayMap(daysOfWeek)

        // Use getNextAlarmDateTime to compute the next time (reuses logic, handles both types)
        val result = getNextAlarmDateTime(
            date = now.toLocalDate(),
            now = now,
            frequency = frequency,
            reminderType = reminderType,
            daysOfWeek = selectedDays,
            daysOfMonth = daysOfMonth
        )

        if (!result.error) {
            val alarmScheduler = AndroidAlarmScheduler(context)
            val alarmItem = AlarmItem(
                id = id,
                nextAlarmDateTime = result.nextDateTime,
                text = text,
                title = title,
                frequency = frequency,
                daysOfWeek = daysOfWeek,
                daysOfMonth = daysOfMonth,
                reminderType = reminderType
            )
            alarmScheduler.schedule(alarmItem)
        }
    }

}