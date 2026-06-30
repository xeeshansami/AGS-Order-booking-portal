package com.agsadil.agssalesandroidclientorderdocter.Fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.agsadil.agssalesandroidclientorderdocter.Activities.NotificationActivity;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Cache the refreshed token so it can be sent to the backend if needed.
        SharedPreferenceManager.getInstance(getApplicationContext()).setFcmToken(token);
        // Keep every device subscribed so broadcast notifications always arrive.
        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = null;
        String body = null;

        // Notification payload (foreground messages / notification-type messages).
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        // Fall back to the data payload (data-only messages) so nothing is missed.
        if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
            if (title == null) title = remoteMessage.getData().get("title");
            if (body == null) body = remoteMessage.getData().get("body");
        }

        if (title == null) title = getString(R.string.app_name);
        if (body == null) body = "";

        sendMyNotification(body, title);
    }

    private void sendMyNotification(String message, String title) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.channel_Id),
                    getResources().getString(R.string.channel_name), NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription(getResources().getString(R.string.channel_description));
            notificationChannel.setName(getResources().getString(R.string.channel_name));
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setOnlyAlertOnce(true)
                .setChannelId(getResources().getString(R.string.channel_Id))
                .setColor(Color.parseColor("#3F5996"));
        //.setProgress(100,50,false);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
