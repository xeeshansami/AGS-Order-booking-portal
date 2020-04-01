package com.ags.agssalesandroidclientorder.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.classes.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.utils.Constant;
import com.ags.agssalesandroidclientorder.utils.FontImprima;
import com.ags.agssalesandroidclientorder.utils.SharedPreferenceManager;
import com.ags.agssalesandroidclientorder.utils.Utils;
import com.ags.agssalesandroidclientorder.utils.setOnitemClickListner;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.ags.agssalesandroidclientorder.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SignupActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private SharedPreferenceHandler sp;

    private String url_Base = "http://mobile.agssukkur.com/agssalesclient.asmx/";
    private String url_Signup = url_Base + "Signup";
    EditText txtFullName;
    EditText txtEmail;
    EditText txtNumber;
    EditText txtUserID;
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
        utils = new Utils();

        setContentView(R.layout.activity_signup);

        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Sign up");
        myToolbar.setNavigationIcon(R.drawable.ic_login);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        hideImage1 = (ImageView) findViewById(R.id.hideshow_img);
        hideimage2 = (ImageView) findViewById(R.id.hideshow_img2);
        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtUserID = (EditText) findViewById(R.id.txtUserID);
        txtNumber = (EditText) findViewById(R.id.txtNumber);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);
        new FontImprima(this,txtFullName);
        new FontImprima(this,txtEmail);
        new FontImprima(this,txtUserID);
        new FontImprima(this,txtNumber);
        new FontImprima(this,txtPassword);
        new FontImprima(this,txtRePassword);
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
            }
        });

    }

    public void CreateAccount(View v) {
        if (validation()) {
            ShowRequestDialog();
            DoSignup();
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
        String fullname = txtFullName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String contact = txtNumber.getText().toString().trim();
        String uid = txtUserID.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();
        String rePwd = txtRePassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            Snackbar.make(findViewById(android.R.id.content), "Full name should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Snackbar.make(findViewById(android.R.id.content), "Email should not be empty", 1000).show();
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Snackbar.make(findViewById(android.R.id.content), "Please enter valid email address", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(contact)) {
            Snackbar.make(findViewById(android.R.id.content), "Contact should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(uid)) {
            Snackbar.make(findViewById(android.R.id.content), "Username should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            Snackbar.make(findViewById(android.R.id.content), "Password should not be empty", 1000).show();
            return false;
        } else if (pwd.length() < 6) {
            Snackbar.make(findViewById(android.R.id.content), "Password should be atleast 6 characters", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(rePwd)) {
            Snackbar.make(findViewById(android.R.id.content), "Confirm password should not be empty", 1000).show();
            return false;
        } else if (!pwd.equals(rePwd)) {
            Snackbar.make(findViewById(android.R.id.content), "Password & confirm password not matched", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    private void DoSignup() {

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
                                sp.setusername(jsonObject.getString("fullname"));
                                sp.setemail(jsonObject.getString("email"));
                                sp.setContact(jsonObject.getString("contact"));
                                sp.setuserid((String) jsonObject.get("uid"));
                                sp.setpassword(txtPassword.getText().toString());
                                ShowDialog("Signup", "your account successfully created!!!");
                                DateFormat df = new SimpleDateFormat("dd-M-yy HH"); // Format time
                                String currentTime = df.format(Calendar.getInstance().getTime());
                                SharedPreferenceManager.getInstance(SignupActivity.this).storeStringInSharedPreferences(Constant.signupTime, currentTime);
                                SharedPreferenceManager.getInstance(SignupActivity.this).storeIntInSharedPreferences(Constant.signupTime2, 0);
                                utils.alertBox(SignupActivity.this, "Congratulation", "Signup Successfully, Kinldy contact your administrator", "OK", new setOnitemClickListner() {
                                    @Override
                                    public void onClick(DialogInterface view, int i) {
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        view.dismiss();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            HideDialog();
                            Toast.makeText(SignupActivity.this, "Some error occured. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HideDialog();
                Toast.makeText(SignupActivity.this, "Some error occured. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}