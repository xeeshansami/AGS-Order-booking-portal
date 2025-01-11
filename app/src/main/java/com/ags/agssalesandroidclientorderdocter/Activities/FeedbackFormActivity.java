package com.ags.agssalesandroidclientorderdocter.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

public class FeedbackFormActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    Button sendFeedback_btn;
    EditText txt_feeback;
    TextView countWords;
    RadioButton bug, suggestion, other;
    final  int count =4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeback_form);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        ImageView cancel_button = findViewById(R.id.cancel_button);
        countWords = findViewById(R.id.countWords);
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
                        sendFeeBack("suggestion");
                    } else {
                        sendFeeBack("other");
                    }
                }
            }
        });
        txt_feeback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int result=count-txt_feeback.length();
                countWords.setText(result+"/4000 Characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
            AGSStore.getInstance().postFeedBack(sp.getuserid(), sp.getusername(), subject, sp.getcategory(), txtFeedBack,sp.getbranch(), new callback() {
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
