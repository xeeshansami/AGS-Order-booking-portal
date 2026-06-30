package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class myApplication extends Application {
    private static myApplication consumerApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        consumerApplication = this;
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                        }
                    }
                });
      /*  FirebaseMessaging.getInstance().subscribeToTopic("my")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                        }
                    }
                });*/
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
//
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        Log.d("FCM", "Token: " + token);
//                        SharedPreferenceManager.getInstance(getApplicationContext()).setFcmToken(token);
//                        // send token to server
//                    }
//                });

    }
}
