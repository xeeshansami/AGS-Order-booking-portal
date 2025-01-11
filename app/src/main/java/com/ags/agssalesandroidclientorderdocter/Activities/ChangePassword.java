package com.ags.agssalesandroidclientorderdocter.Activities;

import android.app.ProgressDialog;
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
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.FontImprima;
import com.ags.agssalesandroidclientorderdocter.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;


public class ChangePassword extends AppCompatActivity {

    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    private String url_Base = "http://mobile.agssukkur.com/agssalesclient.asmx/";
    private String url_Signup = url_Base + "Signup";
    EditText txtPassword;
    EditText txtRePassword;
    ImageView hideImage1, hideimage2;
    ProgressDialog progressDialog;
    boolean showHide = true, showHide2 = true;
    Utils utils;

    public void HideDialog() {
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        setContentView(R.layout.activity_changepassword);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Change Password");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
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
                        if(getIntent().hasExtra("userid")) {
                            String username = getIntent().getStringExtra("userid");
                            String password = txtRePassword.getText().toString().trim();
                            updatePwd(password, username);
                        }else{

                        }
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
        ShowDialog("Requesting", "Sending request to administrator");
    }

    private void ShowDialog(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    public boolean validation() {
        String pwd = txtPassword.getText().toString().trim();
        String rePwd = txtRePassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            txtPassword.setFocusable(true);
            txtPassword.setError("Password should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Password should not be empty", 1000).show();
            return false;
        } else if (pwd.length() < 6) {
            txtPassword.setFocusable(true);
            txtPassword.setError("Password should be atleast 6 characters");
            Snackbar.make(findViewById(android.R.id.content), "Password should be atleast 6 characters", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(rePwd)) {
            txtRePassword.setFocusable(true);
            txtRePassword.setError("Confirm password should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Confirm password should not be empty", 1000).show();
            return false;
        } else if (!pwd.equals(rePwd)) {
            txtPassword.setFocusable(true);
            txtPassword.setError("Password & confirm password not matched");
            txtRePassword.setText("");
            Snackbar.make(findViewById(android.R.id.content), "Password & confirm password not matched", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    public void updatePwd(final String pwd, final String uid) {
        utils.showLoader(this);
        AGSStore.getInstance().setUpdatePwd(pwd, uid, new callback() {
            @Override
            public void Success(String response) {
                utils.hideLoader();
                if(sp.getuserid()!=null && sp.getuserid().equalsIgnoreCase(uid)) {
                    sp.setpassword(pwd);
                }
                utils.alertBox(ChangePassword.this, "Congratulations!", "Your password has been updated", "ok", new setOnitemClickListner() {
                    @Override
                    public void onClick(DialogInterface view, int i) {
                        onBackPressed();
                        view.dismiss();
                    }
                });
            }

            @Override
            public void Failure(ErrorResponse response) {
                Toast.makeText(ChangePassword.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                utils.hideLoader();
            }
        });
    }

    public void onBackPressed() {
        finish();
    }
}