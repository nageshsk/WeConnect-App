package com.projects.sharathnagendra.weconnect;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Sharath Nagendra on 10/23/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
@Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {

        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("email"));

    }

    private void showNotification(String email){

        Intent i = new Intent(this,OrganizationSignup.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT );
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this).setAutoCancel(true).setContentTitle("push notification").setContentText(email ).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);

        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());


    }
}
