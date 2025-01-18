package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Utils.*;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import com.ags.agssalesandroidclientorderdocter.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Paxees on 9/3/2018.
 */
public class SplashScreen extends AppCompatActivity {
    //Splash time 1sec
    private static int SPLASH_TIME_OUT = 3000;
    TextView splashTV, splashTV2;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener fbAuthListener;
    SessionManager session;
    Intent intent;
    TextView poweredBy;
    ImageView splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        //get Splash Screen into the mobile screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        splashLayout = findViewById(R.id.splashLayout);
        splashTV = findViewById(R.id.welcome_tv1);
        splashTV2 = findViewById(R.id.welcome_tv2);
        poweredBy = findViewById(R.id.poweredBy);
        new FontImprima(this, splashTV);
        new FontImprima(this, splashTV2);
        new FontImprima(this, poweredBy);
        loadBackgroundImage();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            poweredBy.setText(Html.fromHtml("<h6>Powered By<font color=#FF0000><u> Paxees Technologies </u></font> © 2025</h6>", Html.FROM_HTML_MODE_COMPACT));
        }
        //Check if user is already logged in or not
        session = new SessionManager(this);
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
    private void loadBackgroundImage() {
        String imageUrl = "https://mobile.agssukkur.com/Images/abcimage.png";

        new Thread(() -> {
            try {
                // Initialize URL connection
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // If you need to parse the response, handle it here
                    runOnUiThread(() ->
                            Glide.with(this)
                                    .load(imageUrl) // Directly load the image
                                    .into(splashLayout) // Your ImageView
                    );
                } else {
                    // Handle server error response
                    runOnUiThread(() -> splashLayout.setImageResource(R.drawable.bg_medical));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Fallback to default image in case of exception
                runOnUiThread(() -> splashLayout.setImageResource(R.drawable.bg_medical));
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
