package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Adapters.SelectOrderListAdapter;
import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityOrder;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;

import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListener2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityOrderProductsDetail extends AppCompatActivity {

    private ArrayList<EntityProductDetails> productsList = new ArrayList<EntityProductDetails>();
    private RecyclerView recycler;
    private SelectOrderListAdapter adapter;
    private ArrayList<EntityOrder> orderList;
    DatabaseHandler db;
    Integer orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_products_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHandler(this);
        orderList = new ArrayList<EntityOrder>();
        getData();
    }

    public void getData() {
        productsList.clear();
        if (getIntent().hasExtra("OrderId")) {
            orderId = getIntent().getIntExtra("OrderId", 0);
            orderList = db.getAllOrders("0");
            productsList.addAll(db.GetOrderDetails(orderId));
        }
        recycler = (RecyclerView) findViewById(R.id.lstOrderProducts);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectOrderListAdapter(this, productsList, new onItemClickListener2() {
            @Override
            public void onItemClick(View view, int position, EntityProductDetails order) {
                if (orderList.size() > 0) {
                    for (EntityOrder entityOrder : orderList) {
                        if (entityOrder.getOrderId().equalsIgnoreCase(String.valueOf(orderId))) {
                            Intent intent = new Intent(ActivityOrderProductsDetail.this, EditSingleProduct.class);
                            intent.putExtra("order", order);
                            startActivity(intent);
                            break;
                        } else {
                            Toast.makeText(ActivityOrderProductsDetail.this, "This order has posted it cannot be edited. please select another one thanks!.", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(ActivityOrderProductsDetail.this, "This order has posted it cannot be edited. please select another one thanks!.", Toast.LENGTH_LONG).show();
                }
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
