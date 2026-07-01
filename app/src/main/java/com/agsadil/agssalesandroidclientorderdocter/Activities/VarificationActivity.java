package com.agsadil.agssalesandroidclientorderdocter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Step 2 of the password reset flow: verify the Firebase phone OTP.
 * Receives "verificationId", "userid" and "usernumber" from ForgetActivity.
 */
public class VarificationActivity extends AppCompatActivity {

    private SharedPreferenceHandler sp;
    private Utils utils;
    private FirebaseAuth firebaseAuth;

    EditText txtCode;
    Button forget_btn;
    TextView sendCodeAgain;

    private String verificationId;
    private String userId;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        AppCompatDelegate.setDefaultNightMode(
                sp.isDarkMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_verification);

        utils = new Utils(this);
        firebaseAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");
        userId = getIntent().getStringExtra("userid");
        phone = getIntent().getStringExtra("usernumber");

        Toolbar myToolbar = findViewById(R.id.toolbar);
        sendCodeAgain = findViewById(R.id.sendCodeAgain);
        txtCode = findViewById(R.id.txtUserName);
        forget_btn = findViewById(R.id.forget_btn);
        myToolbar.setSubtitle("Verification");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setNavigationOnClickListener(v -> finish());

        sendCodeAgain.setOnClickListener(v -> resendCode());

        forget_btn.setOnClickListener(v -> {
            if (validation()) {
                verifyCode(txtCode.getText().toString().trim());
            }
        });
    }

    private void verifyCode(String code) {
        if (TextUtils.isEmpty(verificationId)) {
            Toast.makeText(this, "Verification session expired, please request a new code.", Toast.LENGTH_LONG).show();
            return;
        }
        utils.showLoader(this);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    utils.hideLoader();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(VarificationActivity.this, ChangePassword.class);
                        intent.putExtra("userid", userId);
                        intent.putExtra("usernumber", phone);
                        startActivity(intent);
                        finish();
                    } else {
                        utils.alertBox(VarificationActivity.this, "Alert",
                                "Verification code is invalid. Please try again.", "ok",
                                (view, i) -> view.dismiss());
                    }
                });
    }

    private void resendCode() {
        if (TextUtils.isEmpty(phone)) return;
        utils.showLoader(this);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener(VarificationActivity.this, task -> {
                                    utils.hideLoader();
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(VarificationActivity.this, ChangePassword.class);
                                        intent.putExtra("userid", userId);
                                        intent.putExtra("usernumber", phone);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        utils.hideLoader();
                        Toast.makeText(VarificationActivity.this,
                                e.getMessage() != null ? e.getMessage() : "Could not resend the code.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        utils.hideLoader();
                        verificationId = newVerificationId;
                        Toast.makeText(VarificationActivity.this,
                                "Code has been sent again, please check your phone.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public boolean validation() {
        String code = txtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            txtCode.setError("Enter the code please");
            Snackbar.make(findViewById(android.R.id.content), "Enter the code please", 1000).show();
            return false;
        } else if (code.length() < 6) {
            txtCode.setError("Enter the 6 digit code");
            Snackbar.make(findViewById(android.R.id.content), "Enter the 6 digit code", 1000).show();
            return false;
        }
        return true;
    }
}
