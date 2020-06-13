package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;
import java.util.Random;

public class Reciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder n=new NotificationCompat.Builder(context,"notifyLemubit")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("task")
                .setContentText(intent.getStringExtra("text"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat manager=NotificationManagerCompat.from(context);
int id=Integer.parseInt(intent.getStringExtra("id"));
        manager.notify(id,n.build());

    }
}
