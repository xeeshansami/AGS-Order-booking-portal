package com.ags.agssalesandroidclientorder.Utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    SharedPreferenceManager.getInstance(getApplicationContext()).setFcmToken(task.getResult().getToken());
                    Log.i("firebasetoken", " = " + task.getResult().getToken());
                }
            }
        });
    }
}
