package com.codewithdipesh.habitized.data.alarmManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.data.ALARM_NOTIFICATION_ID
import com.codewithdipesh.habitized.domain.alarmManager.AlarmItem
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.presentation.util.Days
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.SingleString
import com.codewithdipesh.habitized.presentation.util.toDaysOfWeek
import com.codewithdipesh.habitized.presentation.util.toLocalTime
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //check first
        if(context == null || intent == null) return
        //extracting
        val title = intent.getStringExtra("title") ?: ""
        val text =  intent.getStringExtra("text") ?: "Don't let the streak die"
        val frequency =  intent.getStringExtra("frequency") ?: ""
        val reminderTime =  intent.getStringExtra("reminderTime") ?: ""
        val daysOfWeek =  intent.getIntArrayExtra("daysOfWeek")?.toList() ?: emptyList<Int>()
        val daysOfMonth =  intent.getIntArrayExtra("daysOfMonth")?.toList() ?:emptyList<Int>()

        if(title.isEmpty()) return
        //notification
        showNotification(context,title,text)

        //schedule next alarm
        if(frequency.isNotEmpty() && reminderTime.isNotEmpty()
            && daysOfWeek.isNotEmpty() && daysOfMonth.isNotEmpty()
        ){
            scheduleNextAlarm(
                context,
                title,
                text,
                Frequency.fromString(frequency),
                LocalDateTime.now(),
                reminderTime.toLocalTime(),
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
        title : String,
        text : String,
        frequency: Frequency,
        now: LocalDateTime,
        reminderTime: LocalTime,
        daysOfWeek: List<Int>,
        daysOfMonth: List<Int>
    ) {
        Log.d("alarm", "next schedule called")
        val selectedDays = IntToWeekDayMap(daysOfWeek)
        var nextDateTime: LocalDateTime = LocalDateTime.now()
        var error = false
        when (frequency) {
            Frequency.Daily -> {
                nextDateTime = nextDateTime.plusDays(1)
            }
            Frequency.Weekly -> {
                var foundNextDay = false

                Days.entries.forEach { day ->
                    if (selectedDays[day] == true && !foundNextDay) {
                        val nextDate =
                            now.toLocalDate().with(TemporalAdjusters.nextOrSame(day.toDaysOfWeek()))
                        val potentialDateTime = LocalDateTime.of(nextDate, reminderTime)

                        if (!potentialDateTime.isBefore(now)) {
                            nextDateTime = potentialDateTime
                            foundNextDay = true
                        }
                    }
                }
                // If no suitable day found this week, find the first day next week
                if (!foundNextDay) {
                    Days.entries.forEach { day ->
                        if (selectedDays[day] == true && !foundNextDay) {
                            val nextDate = now.toLocalDate().plusWeeks(1)
                                .with(TemporalAdjusters.nextOrSame(day.toDaysOfWeek()))
                            nextDateTime = LocalDateTime.of(nextDate, reminderTime)
                            foundNextDay = true
                        }
                    }
                }
            }
            Frequency.Monthly -> {
                val selectedDays = daysOfMonth // List<Int>
                var earliestDateTime: LocalDateTime? = null

                selectedDays.forEach { dayOfMonth ->
                    try {
                        var nextDate = now.toLocalDate().withDayOfMonth(dayOfMonth)

                        // If the date has passed this month, move to next month
                        if (nextDate.isBefore(now.toLocalDate()) ||
                            (nextDate.isEqual(now.toLocalDate()) && reminderTime!!.isBefore(now.toLocalTime()))
                        ) {
                            nextDate = nextDate.plusMonths(1).withDayOfMonth(dayOfMonth)
                        }

                        val potentialDateTime = LocalDateTime.of(nextDate, reminderTime)

                        // Keep the earliest valid date
                        if (earliestDateTime == null || potentialDateTime.isBefore(earliestDateTime)) {
                            earliestDateTime = potentialDateTime
                        }
                    } catch (e: DateTimeException) {
                        // Handle invalid dates (e.g., Feb 30th)
                        // Skip this day of month
                        error = true
                    }
                }

                earliestDateTime?.let { nextDateTime = it }
            }
        }
        Log.d("alarm", "next schedule : $nextDateTime")
        val alarmManager = AndroidAlarmScheduler(context)
        val alarmItem = AlarmItem(
            time = nextDateTime,
            text = text,
            title = title,
            frequency = frequency,
            daysOfWeek = daysOfWeek,
            daysOfMonth = daysOfMonth
        )
        alarmManager.schedule(alarmItem)
    }

}