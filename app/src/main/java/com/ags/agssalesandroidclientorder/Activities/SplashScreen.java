package com.ags.agssalesandroidclientorder.Activities;

import com.ags.agssalesandroidclientorder.Utils.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.ags.agssalesandroidclientorder.R;


/**
 * Created by Paxees on 9/3/2018.
 */
public class SplashScreen extends AppCompatActivity {
    //Splash time 1sec
    private static int SPLASH_TIME_OUT = 2000;
    TextView splashTV, splashTV2;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener fbAuthListener;
    SessionManager session;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        //get Splash Screen into the mobile screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        splashTV = findViewById(R.id.welcome_tv1);
        splashTV2 = findViewById(R.id.welcome_tv2);
        new FontImprima(this, splashTV);
        new FontImprima(this, splashTV2);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            intent = new Intent(getApplicationContext(), DashboardActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
