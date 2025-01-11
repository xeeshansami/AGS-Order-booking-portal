package com.ags.agssalesandroidclientorderdocter.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorderdocter.Adapters.NotificationAdapter;
import com.ags.agssalesandroidclientorderdocter.Models.Notifications;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    Utils utils;
    Notifications notificationsData;
    TextView title1, title2, addedOn, descriptions, companyname, validTill, title3;
    ImageView notification_image;
    Button order_from_notifications;
    SharedPreferenceHandler sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_details);
        notificationsData = new Notifications();
        sp = new SharedPreferenceHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Notifications Detail");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        order_from_notifications = findViewById(R.id.order_from_notifications);
        notification_image = findViewById(R.id.notification_image);
        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);
        title3 = findViewById(R.id.remarks);
        descriptions = findViewById(R.id.description);
        addedOn = findViewById(R.id.added_on);
        companyname = findViewById(R.id.company_name);
        validTill = findViewById(R.id.valid_till);
        utils = new Utils(this);
        if (getIntent().hasExtra("notification")) {
            notificationsData = (Notifications) getIntent().getExtras().getSerializable("notification");
            Glide.with(this).load(notificationsData.getPicURL()).into(notification_image);
            title1.setText(notificationsData.getTitle1());
            title2.setText(notificationsData.getTitle2());
            title3.setText(notificationsData.getTitle3());
            descriptions.setText(notificationsData.getDescription());
            addedOn.setText(getDate(notificationsData.getAddedOn()));
            companyname.setText(notificationsData.getCompanyName());
            validTill.setText(getDate(notificationsData.getOfferExpiry()));
        }
        order_from_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp.getusername() != null && !TextUtils.isEmpty(sp.getusername())) {
                    startActivity(new Intent(NotificationDetails.this, OrderFormActivity.class));
                    finish();
                } else {
                    utils.alertBox(NotificationDetails.this, "Alert", "Please login first for your order", "Ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            startActivity(new Intent(NotificationDetails.this, LoginActivity.class));
                        }
                    });
                }
            }
        });
    }

    public String getDate(String mydate) {
        SimpleDateFormat sdf = new SimpleDateFormat("M/DD/YYYY HH:MM:SS");
        Date date = null;
        try {
            date = sdf.parse(mydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy h:ss a");
        String myDate = dateFormat.format(new Date(date.getTime()));
        return myDate;
    }
}
