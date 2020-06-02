package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ags.agssalesandroidclientorder.Adapters.NotificationAdapter;
import com.ags.agssalesandroidclientorder.Adapters.OrderListAdapter;
import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.Notifications;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.onItemClickListener;

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
        notifications=new ArrayList<>();
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
                        product.setCompanyName(jObject.get("CompanyName").toString());
                        product.setDescription(jObject.get("Description").toString());
                        product.setAddedOn(jObject.get("AddedOn").toString());
                        product.setPicURL(jObject.get("Pic_URL").toString());
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

    private void notifications(ArrayList<Notifications> notifications) {
        recyclerView = (RecyclerView) findViewById(R.id.notification_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notifications, new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position, EntityOrder order, ImageView imageView) {
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
