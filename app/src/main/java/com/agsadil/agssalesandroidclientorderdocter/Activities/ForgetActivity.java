package com.agsadil.agssalesandroidclientorderdocter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agsadil.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.agsadil.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.agsadil.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Step 1 of the password reset flow.
 * 1. User enters username/userid + mobile number.
 * 2. We verify the pair against the backend (placeholder GET API for now).
 * 3. We send a Firebase phone OTP and move to the verification screen.
 */
public class ForgetActivity extends AppCompatActivity {

    // TODO: set to false once the real "verify user" GET API is provided.
    // While true, the backend verification step is skipped so the OTP flow
    // can be tested end-to-end.
    private static final boolean DUMMY_RESET = true;

    private SharedPreferenceHandler sp;
    private Utils utils;
    private FirebaseAuth firebaseAuth;

    EditText txtUserName, txtUserNumber;
    Button forget_btn;

    private String e164Phone;       // +92XXXXXXXXXX
    private String resolvedUserId;  // returned by the verify API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        firebaseAuth = FirebaseAuth.getInstance();

        AppCompatDelegate.setDefaultNightMode(
                sp.isDarkMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserNumber = findViewById(R.id.txtUserNumber);
        forget_btn = findViewById(R.id.forget_btn);
        myToolbar.setSubtitle("Forget Password");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setNavigationOnClickListener(v -> finish());

        forget_btn.setOnClickListener(v -> {
            if (validation()) {
                verifyUserThenSendOtp();
            }
        });
    }

    public boolean validation() {
        String usernumber = txtUserNumber.getText().toString().trim();
        String username = txtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(usernumber)) {
            txtUserNumber.setError("User number should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "User number should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(username)) {
            txtUserName.setError("User name should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "User name should not be empty", 1000).show();
            return false;
        } else if (usernumber.length() < 11) {
            txtUserNumber.setError("User number should be at least 11 numbers");
            Snackbar.make(findViewById(android.R.id.content), "User number should be at least 11 numbers", 1000).show();
            return false;
        }
        return true;
    }

    /** Converts a local number (03XXXXXXXXX) to E.164 (+923XXXXXXXXX). */
    private String toE164(String number) {
        String n = number.trim().replaceAll("[\\s-]", "");
        if (n.startsWith("+")) return n;
        if (n.startsWith("03")) return "+92" + n.substring(1);   // 03XX -> +923XX
        if (n.startsWith("92")) return "+" + n;
        if (n.startsWith("0")) return "+92" + n.substring(1);
        return "+92" + n;
    }

    private void verifyUserThenSendOtp() {
        final String username = txtUserName.getText().toString().trim();
        final String number = txtUserNumber.getText().toString().trim();
        e164Phone = toE164(number);
        utils.showLoader(this);

        if (DUMMY_RESET) {
            // Placeholder: accept the entered user and proceed to OTP.
            resolvedUserId = username;
            sendOtp();
            return;
        }

        AGSStore.getInstance().verifyUserForReset(e164Phone, username, new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String userid = obj.optString("userid", "0");
                    if (!"0".equalsIgnoreCase(userid)) {
                        resolvedUserId = userid;
                        sendOtp();
                    } else {
                        utils.hideLoader();
                        utils.alertBox(ForgetActivity.this, "Alert",
                                "This user number or username is invalid. Please try again.", "ok",
                                (view, i) -> view.dismiss());
                    }
                } catch (Exception e) {
                    utils.hideLoader();
                    Toast.makeText(ForgetActivity.this, "Unexpected response, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                utils.hideLoader();
                Toast.makeText(ForgetActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                e164Phone, 60, TimeUnit.SECONDS, this, otpCallbacks);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks otpCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    // Instant / auto-retrieval: verify silently and skip code entry.
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(ForgetActivity.this, task -> {
                                utils.hideLoader();
                                if (task.isSuccessful()) {
                                    goToChangePassword();
                                } else {
                                    Toast.makeText(ForgetActivity.this, "Verification failed, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    utils.hideLoader();
                    utils.alertBox(ForgetActivity.this, "Verification failed",
                            e.getMessage() != null ? e.getMessage() : "Could not send the OTP. Please try again.",
                            "ok", (view, i) -> view.dismiss());
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    utils.hideLoader();
                    Toast.makeText(ForgetActivity.this, "OTP sent to " + e164Phone, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetActivity.this, VarificationActivity.class);
                    intent.putExtra("verificationId", verificationId);
                    intent.putExtra("userid", resolvedUserId);
                    intent.putExtra("usernumber", e164Phone);
                    startActivity(intent);
                    finish();
                }
            };

    private void goToChangePassword() {
        Intent intent = new Intent(ForgetActivity.this, ChangePassword.class);
        intent.putExtra("userid", resolvedUserId);
        intent.putExtra("usernumber", e164Phone);
        startActivity(intent);
        finish();
    }
}
