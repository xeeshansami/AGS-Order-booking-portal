package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

public class FeedbackFormActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    Button sendFeedback_btn;
    EditText txt_feeback;
    RadioButton bug, suggestion, other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeback_form);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        ImageView cancel_button = findViewById(R.id.cancel_button);
        txt_feeback = findViewById(R.id.txt_feeback);
        sendFeedback_btn = findViewById(R.id.sendFeedback_btn);
        bug = findViewById(R.id.bug);
        suggestion = findViewById(R.id.suggestion);
        other = findViewById(R.id.other);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendFeedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (bug.isChecked()) {
                        sendFeeBack("bug");
                    } else if (suggestion.isChecked()) {
                        sendFeeBack("bug");
                    } else {
                        sendFeeBack("other");
                    }
                }
            }
        });
    }

    public boolean validation() {
        String txtFeedBack = txt_feeback.getText().toString().trim();
        if (TextUtils.isEmpty(txtFeedBack)) {
            txt_feeback.setFocusable(true);
            txt_feeback.setError("Please enter the description");
            Snackbar.make(findViewById(android.R.id.content), "Please enter the description", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    public void sendFeeBack(String subject) {
        String txtFeedBack = txt_feeback.getText().toString().trim();
        if (sp.getusername() != null) {
            utils.showLoader(this);
            AGSStore.getInstance().postFeedBack(sp.getuserid(), sp.getusername(), subject, sp.getcategory(), txtFeedBack, new callback() {
                @Override
                public void Success(String response) {
                    utils.hideLoader();
                    Toast.makeText(FeedbackFormActivity.this, "Thank you!, your feedback has been sent.", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void Failure(ErrorResponse response) {
                    utils.hideLoader();
                    Toast.makeText(FeedbackFormActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            utils.alertBox(this, "Alert", "Something went wrong, please try again later", "ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }
}
