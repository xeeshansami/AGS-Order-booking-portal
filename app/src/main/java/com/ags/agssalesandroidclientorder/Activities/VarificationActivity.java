package com.ags.agssalesandroidclientorder.Activities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class VarificationActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    EditText txtUserNumber;
    Button forget_btn;
    TextView sendCodeAgain;
    private final static int SEND_SMS_PERMISSION_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        sendCodeAgain = findViewById(R.id.sendCodeAgain);
        txtUserNumber = findViewById(R.id.txtUserName);
        forget_btn = findViewById(R.id.forget_btn);
        myToolbar.setSubtitle("Verification");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendCodeAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("usernumber")) {
                    final String random = String.format("%04d", new Random().nextInt(10000));
                    String messageToSend = "Your OTP for AGS mobile app is :" + random + "\n" +
                            "Warning! Do not share your OTP with anyone.";
                    sp.setRandomNumber(random);
                    SmsManager.getDefault().sendTextMessage(getIntent().getStringExtra("usernumber"), null, messageToSend, null, null);
                    Toast.makeText(VarificationActivity.this, "Code has been sent again, Please check your phone", Toast.LENGTH_LONG).show();
                }
            }
        });
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = txtUserNumber.getText().toString().trim();
                if (validation()) {
                    if (code.equalsIgnoreCase(sp.getRandomNumber())) {
                        utils.alertBox(VarificationActivity.this, "Congratulations!", "Your verification has been succeeded", "Ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                Intent intent = new Intent(VarificationActivity.this, ChangePassword.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                if (getIntent().hasExtra("userid")) {
                                    intent.putExtra("userid", getIntent().getStringExtra("userid"));
                                }
                                startActivity(intent);
                                finish();
                                view.dismiss();
                            }
                        });
                    } else {
                        utils.alertBox(VarificationActivity.this, "Alert", "Verification code is invalid", "ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }

    public boolean validation() {
        String usernumber = txtUserNumber.getText().toString().trim();
        if (TextUtils.isEmpty(usernumber)) {
            txtUserNumber.setError("Enter the code please");
            txtUserNumber.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Enter the code please", 1000).show();
            return false;
        } else if (usernumber.length() < 4) {
            txtUserNumber.setFocusable(true);
            txtUserNumber.setError("Enter the 4 digits code");
            Snackbar.make(findViewById(android.R.id.content), "Enter the 4 digits code", 1000).show();
            return false;
        } else {
            return true;
        }
    }
}
