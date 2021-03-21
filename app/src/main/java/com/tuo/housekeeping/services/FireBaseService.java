package com.tuo.housekeeping.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tuo.housekeeping.Activity_Dashboard;
import com.tuo.housekeeping.R;

import java.util.List;

public class FireBaseService extends FirebaseMessagingService {

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        handleNotification(remoteMessage.getData().get("message"));



    }
    private void handleNotification(String message) {
        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            Intent intent = new Intent( this , Activity_Dashboard.class );
            intent.putExtra("message", message);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                    .setSmallIcon(R.mipmap.ic_housekeepingicon)
                    .setContentTitle("Android Tutorial Point FCM Tutorial")
                    .setContentText(message)
                    .setAutoCancel( true )
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, mNotificationBuilder.build());

        }else{
            // If the app is in background, firebase itself handles the notification
            Log.e("TAG_DEBUG", "handleNotification: "+"background" );
        }
    }
    public static boolean isAppIsInBackground(Context context) {
        Log.e("", "isAppIsInBackground: "+"show" );
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        processInfo.processName.equals(context.getPackageName())) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

}
