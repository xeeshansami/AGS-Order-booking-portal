package com.agsadil.agssalesandroidclientorderdocter.Network;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


public class MyApplication extends Application {
    private static MyApplication consumerApplication;

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

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        // Fetch the current FCM registration token and cache it. Uses the
        // InstanceId API which is the one available in firebase-messaging 20.1.3.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful() || task.getResult() == null) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM", "Token: " + token);
                        SharedPreferenceManager.getInstance(getApplicationContext()).setFcmToken(token);
                    }
                });
    }
    public static MyApplication getApplication() {
        return consumerApplication;
    }
}
