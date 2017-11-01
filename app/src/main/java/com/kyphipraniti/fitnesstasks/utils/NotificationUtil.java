package com.kyphipraniti.fitnesstasks.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.kyphipraniti.fitnesstasks.R;
import com.kyphipraniti.fitnesstasks.activities.MainActivity;

public class NotificationUtil extends ContextWrapper {
    private NotificationManager mNotificationManager;

    public NotificationUtil(Context context) {
        super(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel taskReminderChannel =
                    new NotificationChannel(
                            Constants.TASK_CHANNEL,
                            getString(R.string.notification_channel_task),
                            NotificationManager.IMPORTANCE_DEFAULT);

            taskReminderChannel.setLightColor(Color.GREEN);

            taskReminderChannel.enableLights(true);
            taskReminderChannel.enableVibration(true);
            taskReminderChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getNotificationManager().createNotificationChannel(taskReminderChannel);
        }
    }


    public Notification.Builder getNotificationTask(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), Constants.TASK_CHANNEL)
                    .setContentTitle("FitTask") //TODO: Change to title of Task when task has title
                    .setContentText(body)
                    .setSmallIcon(getSmallIcon())
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent());
        } else {
            return new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(getSmallIcon())
                    .setAutoCancel(true)
                    .setContentIntent(getPendingIntent());
        }
    }

    private PendingIntent getPendingIntent() {
        Intent openMainIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(openMainIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
    }

    public void notify(int id, Notification.Builder notification) {
        getNotificationManager().notify(id, notification.build());
    }

    private int getSmallIcon() {
        return android.R.drawable.ic_dialog_alert;
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }
}
