package com.kyphipraniti.fitnesstasks.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.kyphipraniti.fitnesstasks.model.Task;
import com.kyphipraniti.fitnesstasks.utils.NotificationUtil;

import java.util.Calendar;
import java.util.Date;

public class WakefulReceiver extends WakefulBroadcastReceiver {

    private AlarmManager mAlarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // We bundle the parcel because of issues with Pending Intent being unable
        // to get custom parcelable objects
        Bundle taskBundle = intent.getBundleExtra("taskBundle");
        Task task = taskBundle.getParcelable("task");
        WakefulReceiver.completeWakefulIntent(context, task);
    }

    public void setAlarm(Context context, Task task) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakefulReceiver.class);
        Bundle taskBundle = new Bundle();
        taskBundle.putParcelable("task", task);
        intent.putExtra("taskBundle", taskBundle);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getDeadline().getTimestamp());
        calendar.add(Calendar.MINUTE, -10);

        Date date = calendar.getTime();

        mAlarmManager.setExact(AlarmManager.RTC, date.getTime(), alarmIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        Log.d("WakefulAlarmReceiver", "{cancelAlarm}");

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WakefulReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        mAlarmManager.cancel(alarmIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static void completeWakefulIntent(Context context, Task task) {
        NotificationUtil notificationUtil = new NotificationUtil(context);
        Notification.Builder notificationBuilder = notificationUtil.getNotificationTask(task.getTitle(),
                task.getAction() + " " + task.getAmount() + " " + task.getUnits());
        notificationUtil.notify(1001, notificationBuilder);
    }
}
