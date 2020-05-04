package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;

public class FeedbackFormActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeback_form);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Product Offers");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
