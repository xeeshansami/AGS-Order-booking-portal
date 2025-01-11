package com.ags.agssalesandroidclientorderdocter.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ags.agssalesandroidclientorderdocter.Adapters.NotificationAdapter;
import com.ags.agssalesandroidclientorderdocter.Models.Notifications;
import com.ags.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;
import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListenerForNotifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    Utils utils;
    ArrayList<Notifications> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Notifications");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        utils = new Utils(this);
        utils.showLoader(this);
        notifications = new ArrayList<>();
        AGSStore.getInstance().getNotifications(new callback() {
            @Override
            public void Success(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.substring(response.indexOf("["), response.indexOf("}]") + 2));
                    //TODO: PRODUCTS
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        Notifications product = new Notifications();
                        product.setActive(jObject.get("Active").toString());
                        product.setCompanyName(jObject.get("CompanyName").toString());
                        product.setDescription(jObject.get("Description").toString());
                        product.setDurationStart(jObject.get("DurationStart").toString());
                        product.setDurationEnd(jObject.get("DurationEnd").toString());
                        product.setOfferExpiry(jObject.get("OfferExpiry").toString());
                        product.setAddedBy(jObject.get("AddedBy").toString());
                        product.setAddedOn(jObject.get("AddedOn").toString());
                        product.setPicURL(jObject.get("Pic_URL").toString());
                        product.setTitle1(jObject.get("Title1").toString());
                        product.setTitle2(jObject.get("Title2").toString());
                        product.setTitle3(jObject.get("Title3").toString());
                        notifications.add(product);
                    }
                    notifications(notifications);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.hideLoader();
            }

            @Override
            public void Failure(ErrorResponse response) {
                utils.hideLoader();
                Toast.makeText(NotificationActivity.this, "Some error occurred in authentication. Kindly inform your administrator.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notifications(final ArrayList<Notifications> notifications) {
        recyclerView = (RecyclerView) findViewById(R.id.notification_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notifications, new onItemClickListenerForNotifications() {
            @Override
            public void onItemClick(View view, int position, Notifications dataOfNotificatons, ImageView imageView) {
                Intent intent = new Intent(NotificationActivity.this, NotificationDetails.class);
                intent.putExtra("notification", dataOfNotificatons);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
