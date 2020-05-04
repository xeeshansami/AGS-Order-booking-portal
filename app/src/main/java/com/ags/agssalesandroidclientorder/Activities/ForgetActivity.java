package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
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

public class ForgetActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    EditText txtUserName;
    Button forget_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUserName = findViewById(R.id.txtUserName);
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
                    forget();
                }
            }
        });
    }

    public boolean validation() {
        String email = txtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            txtUserName.setError("Username should not be empty");
            txtUserName.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Username should not be empty", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    public void forget() {
        String username = txtUserName.getText().toString().trim();
        if (sp.getusername() != null && !TextUtils.isEmpty(sp.getusername()) && username.equalsIgnoreCase(sp.getusername())) {

        } else {
            utils.alertBox(ForgetActivity.this, "Alert", "User: " + sp.getusername() + " is invalid, please enter a valid username again.", "ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }
}
