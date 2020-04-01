package com.ags.agssalesandroidclientorder.Activity;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorder.classes.ProductDetailsListAdapter;

import com.ags.agssalesandroidclientorder.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrderProductsDetail extends AppCompatActivity {

    private List<EntityProductDetails> productsList = new ArrayList<EntityProductDetails>();
    private ListView listView;
    private ProductDetailsListAdapter adapter;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_products_detail);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.lstOrderProducts);
        adapter = new ProductDetailsListAdapter(this, productsList);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        Integer orderId = intent.getIntExtra("OrderId", 0);

        db = new DatabaseHandler(this);
        productsList.addAll(db.GetOrderDetails(orderId));

        adapter.notifyDataSetChanged();
    }
}
