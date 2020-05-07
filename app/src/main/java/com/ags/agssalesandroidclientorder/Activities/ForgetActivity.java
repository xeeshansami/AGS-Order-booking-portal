package com.ags.agssalesandroidclientorder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ForgetActivity extends AppCompatActivity {
    String SENT = "Code has been sent again, Please check your phone";
    String DELIVERED ="Code has not been send due to some problem occurred, please try again later.";
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    EditText txtUserName, txtUserNumber;
    Button forget_btn;
    private final static int SEND_SMS_PERMISSION_REQ = 1;
//    BroadcastReceiver sendBroadcastReceiver = new SentReceiver();
//    BroadcastReceiver deliveryBroadcastReciever = new DeliverReceiver();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserNumber = findViewById(R.id.txtUserNumber);
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
//                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        utils.alertBox(ForgetActivity.this, "Alert", "This sms charge with standard rate apply!", "Yes", "No", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                forget();
                                view.dismiss();
                            }
                        });
//                    } else {
//                        ActivityCompat.requestPermissions(ForgetActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
//                    }

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
            txtUserName.setError("User name should not be empty");
            txtUserName.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "User name should not be empty", 1000).show();
            return false;
        } else if (usernumber.length() < 11) {
            txtUserNumber.setFocusable(true);
            txtUserNumber.setError("User number should be at least 11 numbers");
            Snackbar.make(findViewById(android.R.id.content), "User number should be at least 11 numbers", 1000).show();
            return false;
        }/* else if (!usernumber.startsWith("03")) {
            txtUserNumber.setFocusable(true);
            txtUserNumber.setError("User number starts with 03 format like this 03XXXXXXXXX");
            Snackbar.make(findViewById(android.R.id.content), "User number starts with 03 format like this 03XXXXXXXXX", 1000).show();
            return false;
        }*/ else {
            return true;
        }
    }

    String usernumberReplace;

    public void forget() {
        final String usernumber = txtUserNumber.getText().toString().trim();
        final String username = txtUserName.getText().toString().trim();
        if (usernumber.startsWith("03")) {
            usernumberReplace = usernumber.replace("03", "923");
        }else{
            usernumberReplace=usernumber;
        }
        utils.showLoader(this);
        AGSStore.getInstance().getLoginForPassword(username, usernumberReplace, new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONObject objects = new JSONObject(response);
                    String userid = objects.get("userid").toString();
                    String userphonenumber = objects.get("role").toString();
                    if (!userid.equalsIgnoreCase("0") && !userphonenumber.equalsIgnoreCase("0")) {
                  /*      final String random = String.format("%04d", new Random().nextInt(10000));
                        String messageToSend = "Your OTP for AGS mobile app is :" + random + "\n" + "Warning! Do not share your OTP with anyone.";
                        sp.setRandomNumber(random);
                        PendingIntent sentPI = PendingIntent.getBroadcast(ForgetActivity.this, 0, new Intent(
                                SENT), 0);
                        PendingIntent deliveredPI = PendingIntent.getBroadcast(ForgetActivity.this, 0,
                                new Intent(DELIVERED), 0);
                        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
                        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
                        SmsManager.getDefault().sendTextMessage(usernumber, null, messageToSend, null, null);*/
                        Intent intent = new Intent(ForgetActivity.this, ChangePassword.class);
                        intent.putExtra("userid", userid);
                        intent.putExtra("usernumber", usernumberReplace);
                        startActivity(intent);
//                        Toast.makeText(ForgetActivity.this, "Code has been sent again, Please check your phone", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        utils.alertBox(ForgetActivity.this, "Alert", "This user number or userid is invalid, please enter a valid user number & username again.", "ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.hideLoader();
            }

            @Override
            public void Failure(ErrorResponse response) {
                Toast.makeText(ForgetActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                utils.hideLoader();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
      /*      unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
       /*     unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
         /*   case SEND_SMS_PERMISSION_REQ:
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
                break;*/
        }
    }

  /*  private boolean checkPermission(String sendSms) {
        int checkpermission = ContextCompat.checkSelfPermission(this, sendSms);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }*/

 /*   class SentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(ForgetActivity.this, "Code has been sent again, Please check your phone", Toast.LENGTH_LONG).show();
                *//*    startActivity(new Intent(SendSMS.this, ChooseOption.class));
                    overridePendingTransition(R.anim.animation, R.anim.animation2);*//*
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Sms sending failed", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service available",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    class DeliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(ForgetActivity.this, "Code has been sent again, Please check your phone", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(ForgetActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }*/
}
