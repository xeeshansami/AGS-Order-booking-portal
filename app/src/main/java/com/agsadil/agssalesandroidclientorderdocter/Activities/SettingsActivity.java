package com.agsadil.agssalesandroidclientorderdocter.Activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SessionManager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeSwitch;
    private Button chooseColorButton;
    private TextView appNameTextView, appVersionTextView, appSizeTextView, lastUpdateTextView;

    private SessionManager sessionManager;
    private static final String PREFS_NAME = "AppSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Setting");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Initialize views
        darkModeSwitch = findViewById(R.id.switch_dark_mode);
        chooseColorButton = findViewById(R.id.btn_choose_color);
        appNameTextView = findViewById(R.id.tv_app_name);
        appVersionTextView = findViewById(R.id.tv_app_version);
        appSizeTextView = findViewById(R.id.tv_app_size);
        lastUpdateTextView = findViewById(R.id.tv_last_update);

        // Initialize SharedPreferences
        sessionManager = new SessionManager(this);

        // Set default values for app info
        setAppInfo();
        loadUserSettings();

        // Dark Mode toggle listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sessionManager.setDarkMode(true);
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // Enable light mode
                sessionManager.setDarkMode(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Choose color button listener
        chooseColorButton.setOnClickListener(v -> {
            // For simplicity, we'll pick a fixed color here. You can implement a color picker dialog.
            int chosenColor = Color.parseColor("#FF4081"); // Hardcoded color, you can make it dynamic.
            changeAppThemeColor(chosenColor);

            // Save the chosen color to SharedPreferences
            sessionManager.setColorMode(true);
        });
    }

    private void setAppInfo() {
        // Set the application name
        appNameTextView.setText("App Name: " + getString(R.string.app_name));

        // Set the app version
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionTextView.setText("Version: " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Set the app size (approximation)
        long sizeInBytes = getAppSize();
        appSizeTextView.setText("App Size: " + formatSize(sizeInBytes));

        // Set the last update (this would typically be the last time the app was installed/updated)
        lastUpdateTextView.setText("Last Update: " + "2025-01-17"); // Update this value as per your app release
    }

    private long getAppSize() {
        // This method will give an approximate size of the APK
        File apkFile = new File(getApplicationInfo().sourceDir);
        return apkFile.length();
    }

    private String formatSize(long sizeInBytes) {
        long sizeInKB = sizeInBytes / 1024;
        long sizeInMB = sizeInKB / 1024;
        return sizeInMB + "MB";
    }

    private void loadUserSettings() {
        // Load the saved theme mode
        boolean isDarkMode = sessionManager.isDarkMode();
        darkModeSwitch.setChecked(isDarkMode);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Load the saved color preference
//        int themeColor = sessionManager.isColorMode();
//        changeAppThemeColor(themeColor);
    }

    private void changeAppThemeColor(int color) {
        // Apply the chosen color to the app's theme (e.g., background or text color)
        // For simplicity, we're changing the background color of the root layout to the chosen color.
        View rootLayout = findViewById(android.R.id.content);
        rootLayout.setBackgroundColor(color);
    }
}
