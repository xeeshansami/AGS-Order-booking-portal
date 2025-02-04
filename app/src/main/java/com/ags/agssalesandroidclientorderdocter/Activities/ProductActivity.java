package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.R;

import android.app.Activity;

import com.ags.agssalesandroidclientorderdocter.Adapters.ProductListAdapter;
import com.ags.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.ags.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private SessionManager sessionManager;

    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private List<EntityProduct> productsListSP = new ArrayList<EntityProduct>();
    private RecyclerView listView;
    private ProductListAdapter adapter;
    private EditText txtProductSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = new DatabaseHandler(this);
        sessionManager = new SessionManager(this);
        boolean isDarkMode = sessionManager.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        productsList = db.getAllProducts();
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activities window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BindSearchProductTextBox();
        BindProductsList();
    }

    private void BindSearchProductTextBox() {
        txtProductSearch = (EditText) findViewById(R.id.searchProductList);

        txtProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String getText = txtProductSearch.getText().toString();

                productsList = db.getAllProducts(getText);
                BindProductsList();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void BindProductsList() {

        listView = (RecyclerView) findViewById(R.id.lstProducts);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductListAdapter(this, productsList, new OnItemClickListener() {
            @Override
            public void onItemClick(EntityProduct entry) {
                /*Toast.makeText(ProductActivity.this, entry.getName(), Toast.LENGTH_SHORT).show();*/
                Intent returnIntent = new Intent();
                returnIntent.putExtra("productId", String.valueOf(entry.getProductId()));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
