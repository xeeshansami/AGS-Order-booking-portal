package com.ags.agssalesandroidclientorder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class ForgetActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    EditText txtUserName, txtUserNumber;
    Button forget_btn;
    private final static int SEND_SMS_PERMISSION_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserNumber = findViewById(R.id.txtUserName);
        forget_btn = findViewById(R.id.forget_btn);
        myToolbar.setSubtitle("Forget Password");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        utils.alertBox(ForgetActivity.this, "Alert", "This sms charge with standard rate apply!", "Yes", "No", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                forget();
                                view.dismiss();
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(ForgetActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
                    }

                }
            }
        });
    }

    public boolean validation() {
        String usernumber = txtUserNumber.getText().toString().trim();
        String username = txtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(usernumber)) {
            txtUserNumber.setError("User number should not be empty");
            txtUserNumber.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "User number should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(username)) {
            txtUserNumber.setError("User name should not be empty");
            txtUserNumber.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "User name should not be empty", 1000).show();
            return false;
        } else if (usernumber.length() < 11) {
            txtUserNumber.setFocusable(true);
            txtUserNumber.setError("User number should be at least 11 numbers");
            Snackbar.make(findViewById(android.R.id.content), "User number should be at least 11 numbers", 1000).show();
            return false;
        } else if (!usernumber.startsWith("03")) {
            txtUserNumber.setFocusable(true);
            txtUserNumber.setError("User number starts with 03 format like this 03XXXXXXXXX");
            Snackbar.make(findViewById(android.R.id.content), "User number starts with 03 format like this 03XXXXXXXXX", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    public void forget() {
        String usernumber = txtUserNumber.getText().toString().trim();
        String username = txtUserName.getText().toString().trim();
        if (usernumber != null && !TextUtils.isEmpty(usernumber)) {
            final String random = String.format("%04d", new Random().nextInt(10000));
            String messageToSend = "Your OTP for AGS mobile app is :" + random + "\n" +
                    "Warning! Do not share your OTP with anyone.";
            sp.setRandomNumber(random);
            SmsManager.getDefault().sendTextMessage(usernumber, null, messageToSend, null, null);
            Intent intent = new Intent(this, VarificationActivity.class);
            intent.putExtra("userid", username);
            startActivity(intent);
            finish();
        } else {
            {
                utils.alertBox(ForgetActivity.this, "Alert", "This user number is invalid, please enter a valid user number again.", "ok", new setOnitemClickListner() {
                    @Override
                    public void onClick(DialogInterface view, int i) {
                        view.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQ:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    utils.alertBox(ForgetActivity.this, "Alert", "This sms charge with standard rate apply!", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            forget();
                            view.dismiss();
                        }
                    });
                } else {
                    finish();
                }
                break;
        }
    }

    private boolean checkPermission(String sendSms) {
        int checkpermission = ContextCompat.checkSelfPermission(this, sendSms);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }
}
