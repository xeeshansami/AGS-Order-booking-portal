package com.ags.agssalesandroidclientorderdocter.Activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {
    private AGSStore agsStore = AGSStore.getInstance();
    private Utils utils;
    private SharedPreferenceHandler sp;
    EditText login_id,user_name,user_email,user_contact;
    Button update_btn,update_pwd_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Update Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        login_id=findViewById(R.id.login_id);
        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        user_contact=findViewById(R.id.user_contact);
        update_btn=findViewById(R.id.update_btn);
        update_pwd_btn=findViewById(R.id.update_pwd_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);

        String username = sp.getusername();
        if (username != null) {
            login_id.setText(username);
        }

        String userCategory = sp.getUser_Category();
        if (userCategory != null) {
            user_name.setText(userCategory);
        }

        String email = sp.getemail();
        if (email != null) {
            user_email.setText(email);
        } else {
            user_email.setText("");
        }

        String contact = sp.getcontact();
        if (contact != null) {
            user_contact.setText(contact);
        } else {
            user_contact.setText("");
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postUpdateProfile(user_name.getText().toString().trim(), user_email.getText().toString().trim(),
                        user_contact.getText().toString().trim(), sp.getuserid());
            }
        });

        final Intent intent = new Intent(this, ChangePassword.class);
        update_pwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private boolean validation() {
        String fullname = user_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String contact = user_contact.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            user_name.setFocusable(true);
            user_name.setError("Full name should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Full name should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            user_email.setFocusable(true);
            user_email.setError("Email should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Email should not be empty", 1000).show();
            return false;
        } else if (!isValidEmail(email)) {
            user_email.setFocusable(true);
            user_email.setError("Please enter a valid email address");
            Snackbar.make(findViewById(android.R.id.content), "Please enter a valid email address", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(contact)) {
            user_contact.setFocusable(true);
            user_contact.setError("Contact should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Contact should not be empty", 1000).show();
            return false;
        } else if (contact.length() < 11) {
            user_contact.setFocusable(true);
            user_contact.setError("Contact number should be at least 11 numbers");
            Snackbar.make(findViewById(android.R.id.content), "Contact number should be at least 11 numbers", 1000).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_btn:
                break;
        }
    }

    private void postUpdateProfile(String newName, String newEmail, String newNumber, String userid) {
        utils.showLoader(this);

        agsStore.postUpdateProfile(newName, newEmail, newNumber, userid, new callback() {
            @Override
            public void Success(String response) {
                utils.hideLoader();

                try {
                    JSONObject objects = new JSONObject(response);
                    String userId = objects.getString("userid");

                    if (!userId.equalsIgnoreCase("0")) {
                        utils.alertBox(UpdateProfile.this, "Congratulations!", "Your profile has been updated", "ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                onBackPressed();
                                view.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                Toast.makeText(UpdateProfile.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                utils.hideLoader();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
