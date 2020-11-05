package com.cloud6.pargmedia.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BootReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                //To start new intent when phone starts up
                Intent i = new Intent(context, SplashActivity.class);
                // To put activity on the top of the stack since activity is launched from context outside activity
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // EDITED
                i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startActivity(i);
                } else {
                    context.startActivity(i);
                }
            }
        }
        /*int interval=1000;
        public void onReceive(Context context, Intent arg1)
        {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, Sservice.class), PendingIntent.FLAG_UPDATE_CURRENT);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pi);
            Intent intent = new Intent(context,Sservice.class);
            *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }*//*
            Log.i("Autostart", "started");
        }*/
    }
