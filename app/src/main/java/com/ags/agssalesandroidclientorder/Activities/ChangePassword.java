package com.ags.agssalesandroidclientorder.Activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.FontImprima;
import com.ags.agssalesandroidclientorder.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
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
//                        ShowRequestDialog();
                        /*     DoSignup();*/
                        utils.alertBox(ChangePassword.this, "Congratulations!", "Your password has been changed", "Ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                onBackPressed();
                                view.dismiss();
                            }
                        });
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

  /*  private void DoSignup() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = url_Signup
                + "?fullname=" + txtFullName.getText().toString().trim()
                + "&email=" + txtEmail.getText().toString().trim()
                + "&contact=" + txtNumber.getText().toString().trim()
                + "&uid=" + txtUserID.getText().toString().trim()
                + "&pwd=" + txtPassword.getText().toString().trim();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString().substring(response.indexOf("{"), response.indexOf("}") + 1));
                            if (jsonObject.get("uid") != null) {
//                                sp.setusername(jsonObject.getString("fullname"));
//                                sp.setemail(jsonObject.getString("email"));
//                                sp.setContact(jsonObject.getString("contact"));
                                sp.setuserid((String) jsonObject.get("uid"));
//                                sp.setpassword(txtPassword.getText().toString());
                                ShowDialog("Signup", "your account successfully created!!!");
                                DateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss"); // Format time
                                String currentTime = df.format(Calendar.getInstance().getTime());
//                                String temptime="09/4/2020 04:40:00";
                                SharedPreferenceManager.getInstance(ChangePassword.this).storeStringInSharedPreferences(Constant.signupTime, currentTime);
                                utils.alertBox(ChangePassword.this, "Congratulation", "Signup Successfully, Kinldy contact your administrator", "OK", new setOnitemClickListner() {
                                    @Override
                                    public void onClick(DialogInterface view, int i) {
                                        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        view.dismiss();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            HideDialog();
                            Toast.makeText(ChangePassword.this, "Some error occurred. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HideDialog();
                Toast.makeText(ChangePassword.this, "Some error occurred. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}