package com.codewithdipesh.habitized.data.alarmManager

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.codewithdipesh.habitized.domain.alarmManager.AlarmItem
import com.codewithdipesh.habitized.domain.alarmManager.AlarmScheduler
import com.codewithdipesh.habitized.presentation.util.SingleString
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId

class AndroidAlarmScheduler (
    @ApplicationContext private val context : Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val requestCode = item.id.hashCode()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("text",item.text)
            putExtra("title",item.title)
            putExtra("frequency",item.frequency.displayName)
            putExtra("reminderTime",item.time.toLocalTime().SingleString())
            putExtra("daysOfWeek",item.daysOfWeek.toIntArray())
            putExtra("daysOfMonth",item.daysOfMonth.toIntArray())
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}