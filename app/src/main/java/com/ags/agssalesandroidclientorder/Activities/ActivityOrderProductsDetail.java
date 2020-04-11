package com.ags.agssalesandroidclientorder.Activities;

import com.ags.agssalesandroidclientorder.Adapters.SelectOrderListAdapter;
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;

import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.onItemClickListener2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityOrderProductsDetail extends AppCompatActivity {

    private ArrayList<EntityProductDetails> productsList = new ArrayList<EntityProductDetails>();
    private RecyclerView recycler;
    private SelectOrderListAdapter adapter;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_products_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getData();
    }

    public void getData(){
        productsList.clear();
        if(getIntent().hasExtra("OrderId")) {
            Integer orderId = getIntent().getIntExtra("OrderId", 0);
            db = new DatabaseHandler(this);
            productsList.addAll(db.GetOrderDetails(orderId));
        }
        recycler = (RecyclerView) findViewById(R.id.lstOrderProducts);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectOrderListAdapter(this, productsList, new onItemClickListener2() {
            @Override
            public void onItemClick(View view, int position, EntityProductDetails order) {
                Intent intent = new Intent(ActivityOrderProductsDetail.this, EditSingleProduct.class);
                intent.putExtra("order",  order);
                startActivity(intent);
            }
        });
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
