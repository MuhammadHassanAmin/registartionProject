package com.goprogs.riphahportalquiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText(this,"message received",Toast.LENGTH_SHORT).show();
        showNotification(remoteMessage.getNotification().getBody());
    }
    public  void  showNotification(String message){
        PendingIntent p1= PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        Notification notification = new Notification.Builder(this).setContentText(message).setContentIntent(p1)
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);

    }
}
