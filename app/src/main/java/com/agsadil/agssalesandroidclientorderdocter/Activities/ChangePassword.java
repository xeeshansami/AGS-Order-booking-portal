package com.agsadil.agssalesandroidclientorderdocter.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.agsadil.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.agsadil.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.FontImprima;
import com.agsadil.agssalesandroidclientorderdocter.Utils.OnConnectionCallback;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.agsadil.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;


public class ChangePassword extends AppCompatActivity {

    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    private String url_Base = "http://mobile.agssukkur.com/agssalesclient.asmx/";
    private String url_Signup = url_Base + "Signup";
    EditText txtPassword;
    EditText txtRePassword;
    ImageView hideImage1, hideimage2;
    boolean showHide = true, showHide2 = true;
    Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        setContentView(R.layout.activity_changepassword);
        db = new DatabaseHandler(this);
        boolean isDarkMode = sp.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Change Password");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        hideImage1 = (ImageView) findViewById(R.id.hideshow_img);
        hideimage2 = (ImageView) findViewById(R.id.hideshow_img2);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);
        new FontImprima(this, txtPassword);
        new FontImprima(this, txtRePassword);
        hideImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide) {
                    hideImage1.setImageResource(R.drawable.ic_show);
                    txtRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHide = false;
                } else {
                    txtRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hideImage1.setImageResource(R.drawable.ic_hide);
                    showHide = true;
                }
                new FontImprima(ChangePassword.this, txtRePassword);
            }
        });
        hideimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide2) {
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hideimage2.setImageResource(R.drawable.ic_show);
                    showHide2 = false;
                } else {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hideimage2.setImageResource(R.drawable.ic_hide);
                    showHide2 = true;
                }
                new FontImprima(ChangePassword.this, txtPassword);
            }
        });

    }

    public void CreateAccount(View v) {
        if (validation()) {
            changePassword();
        }
    }

    public void changePassword() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    if (validation()) {
                        String uid = getIntent().hasExtra("userid")
                                ? getIntent().getStringExtra("userid")
                                : sp.getuserid();
                        String password = txtRePassword.getText().toString().trim();
                        if (TextUtils.isEmpty(uid)) {
                            Toast.makeText(ChangePassword.this, "Missing user id, please restart the reset process.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        updatePwd(password, uid);
                    }
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.alertBox(ChangePassword.this, "Internet Connections", "Poor connection, check your internet connection is working or not!", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            utils.alertBox(this, "Internet Connections", "Network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    view.dismiss();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                    view.dismiss();
                }
            });
        }
    }

    private void ShowRequestDialog() {
        utils.showLoader(this);
        utils.showDialogUpdateMessage("Requesting\nSending request to administrator ...");
    }


    public boolean validation() {
        String pwd = txtPassword.getText().toString().trim();
        String rePwd = txtRePassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            return fail(txtPassword, "Password should not be empty");
        } else if (pwd.length() < 8) {
            return fail(txtPassword, "Password must be at least 8 characters");
        } else if (!pwd.matches(".*[A-Z].*")) {
            return fail(txtPassword, "Add at least one uppercase letter (A-Z)");
        } else if (!pwd.matches(".*[a-z].*")) {
            return fail(txtPassword, "Add at least one lowercase letter (a-z)");
        } else if (!pwd.matches(".*\\d.*")) {
            return fail(txtPassword, "Add at least one number (0-9)");
        } else if (!pwd.matches(".*[^A-Za-z0-9].*")) {
            return fail(txtPassword, "Add at least one special character (e.g. @ # $ !)");
        } else if (pwd.contains(" ")) {
            return fail(txtPassword, "Password must not contain spaces");
        } else if (TextUtils.isEmpty(rePwd)) {
            return fail(txtRePassword, "Confirm password should not be empty");
        } else if (!pwd.equals(rePwd)) {
            txtRePassword.setText("");
            return fail(txtRePassword, "Password & confirm password do not match");
        }
        return true;
    }

    private boolean fail(EditText field, String message) {
        field.setFocusable(true);
        field.setError(message);
        Snackbar.make(findViewById(android.R.id.content), message, 1000).show();
        return false;
    }

    // TODO: set to false once the real "reset password" GET API is provided.
    private static final boolean DUMMY_RESET = true;

    public void updatePwd(final String pwd, final String uid) {
        utils.showLoader(this);

        if (DUMMY_RESET) {
            // Placeholder: simulate a successful password update.
            utils.hideLoader();
            onPasswordUpdated();
            return;
        }

        AGSStore.getInstance().resetPassword(pwd, uid, new callback() {
            @Override
            public void Success(String response) {
                utils.hideLoader();
                onPasswordUpdated();
            }

            @Override
            public void Failure(ErrorResponse response) {
                Toast.makeText(ChangePassword.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                utils.hideLoader();
            }
        });
    }

    /** Clears the session and sends the user back to the login screen. */
    private void onPasswordUpdated() {
        sp.clearAll();
        utils.alertBox(ChangePassword.this, "Congratulations!",
                "Your password has been updated. Please login again.", "ok",
                new setOnitemClickListner() {
                    @Override
                    public void onClick(DialogInterface view, int i) {
                        view.dismiss();
                        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    public void onBackPressed() {
        finish();
    }
}