package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ags.agssalesandroidclientorder.Adapters.ProductListAdapter;
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProductOfferActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    ListView product_offer_recycler_view;
    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private ProductListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_offer);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        product_offer_recycler_view =  findViewById(R.id.product_offer_recycler_view);
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
        productsList = db.getAllProducts();
        BindProductsList();
    }
    private void BindProductsList(){
        adapter = new ProductListAdapter(this, productsList);
        product_offer_recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
