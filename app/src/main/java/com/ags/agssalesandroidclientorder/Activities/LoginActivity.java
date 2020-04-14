package com.ags.agssalesandroidclientorder.Activities;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;

import android.Manifest;

import com.ags.agssalesandroidclientorder.BuildConfig;

import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.Utils.Constant;
import com.ags.agssalesandroidclientorder.Utils.FontImprima;
import com.ags.agssalesandroidclientorder.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceManager;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ags.agssalesandroidclientorder.R;

public class LoginActivity extends AppCompatActivity {
    Utils utils;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    EditText txtUsername;
    EditText txtPassword;
    Button btnSignup, btnLogin;
    ImageView hideImage1;
    boolean showHide = true;
    Toolbar myToolbar;
    TextView version_name_lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        setContentView(R.layout.activity_login);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        hideImage1 = findViewById(R.id.hideshow_img);
        version_name_lbl = findViewById(R.id.version_name_lbl);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnLogin2);
        version_name_lbl.setText("Version: " + BuildConfig.VERSION_NAME);
        myToolbar.setSubtitle("Sign in");
        myToolbar.setNavigationIcon(R.drawable.ic_login);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setEnabled(false);
                btnLogin.setClickable(false);
                Login(btnLogin);
            }
        });
        signUpBtnCheck();
        new FontImprima(this, txtUsername);
        new FontImprima(this, txtPassword);
        hideImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide) {
                    hideImage1.setImageResource(R.drawable.ic_show);
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHide = false;
                } else {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hideImage1.setImageResource(R.drawable.ic_hide);
                    showHide = true;
                }
                new FontImprima(LoginActivity.this, txtPassword);
            }
        });
    }
    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = startDate.getTime() - endDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        System.out.printf("dateCheck=> %d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays > 0) {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        } else {
            if (elapsedHours < 24) {
                long remHrs = 23 - elapsedHours;
                long remMins = 60 - elapsedMinutes;
                utils.alertBox(this, "Alert", "Your request for sign up has been already submitted from this device. Please contact Admin for quick approval. Re-submission sign up from this device will be enabled after " + remHrs + " hr(s) & " + remMins + " min(s)",
                        "Ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
            } else {
                SharedPreferenceManager.getInstance(LoginActivity.this).removeStringInSharedPreferences(Constant.signupTime, "remove");
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        }
    }
    public void date(String perviousDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        try {
            String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());
            Date date1 = simpleDateFormat.parse(currentTime);
            Date date2 = simpleDateFormat.parse(perviousDate);
            Log.i("currentTime", " = " + date1);
            printDifference(date1, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void signUpBtnCheck() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime)) &&
                        SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime) != null) {
                    String previousTime = SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime);
                    Log.i("previousTime", " = " + previousTime);
                    date(previousTime);
                } else {
                    SharedPreferenceManager.getInstance(LoginActivity.this).removeStringInSharedPreferences(Constant.signupTime, "remove");
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public boolean validation() {
        String email = txtUsername.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            txtUsername.setError("Username should not be empty");
            txtUsername.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Username should not be empty", 1000).show();
            return false;
        } /*else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Snackbar.make(findViewById(android.R.id.content), "Please enter valid email address", 1000).show();
            return false;
        }*/ else if (TextUtils.isEmpty(pwd)) {
            txtPassword.setError("Password should not be empty");
            txtPassword.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Password should not be empty", 1000).show();
            return false;
        } else {
            return true;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkForPermissions();
    }
    public void onResume() {
        super.onResume();
        if (db != null) {
            db.deleteOrdersOlderThenSevenDays();
        }
    }
    public void isOnlineOffline() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    utils.getAppVersion();
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.hideLoader();
                    utils.myLogs(LoginActivity.this, errorMsg, true);

                }
            }).execute();
        }
    }
    private void checkForPermissions() {
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                isOnlineOffline();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                utils.alertBox(LoginActivity.this, "Permission Denied", "Without permission application will not start, Kindly accept first all permission",
                        "Later", "Again", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                                finish();
                            }
                        }, new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                checkForPermissions();
                            }
                        });
            }
        });

    }
    public void online(Button button) {
        String username = txtUsername.getText().toString().trim();
        String userpassword = txtPassword.getText().toString().trim();
        utils.loginOrActiveCheck(false, true, false, button, username, userpassword);
    }
    public void offline(Button button) {
        if (sp.getusername() != null && sp.getusername().equals(txtUsername.getText().toString().trim()) && sp.getpassword() != null && sp.getpassword().equals(txtPassword.getText().toString().trim())) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        } else {
            utils.alertBox(LoginActivity.this, "Alert", "Username or password is invalid please login again and enabled your internet connection", "ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
        utils.hideLoader();
        button.setEnabled(true);
        button.setClickable(true);
    }
    public void Login(final Button btnLogin) {
        if (validation()) {
            if (utils.checkConnection(this)) {
                utils.showLoader(this);
                new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        online(btnLogin);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        offline(btnLogin);
                    }
                }).execute();
            } else {
                offline(btnLogin);
            }
        }
    }
}

