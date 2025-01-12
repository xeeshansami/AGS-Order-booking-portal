package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.R;
import android.app.Activity;

import com.ags.agssalesandroidclientorderdocter.Adapters.ProductListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DatabaseHandler db;

    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private ListView listView;
    private ProductListAdapter adapter;
    private EditText txtProductSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = new DatabaseHandler(this);
        productsList = db.getAllProducts();
        if(getIntent().hasExtra("products")){
            List<EntityProductDetails> list = new Gson().fromJson(
                    getIntent().getStringExtra("products"),
                    new TypeToken<List<EntityProductDetails>>(){}.getType()
            );
            for(EntityProductDetails s : list){
                for(int i=0;i<productsList.size();i++){
                    if(s.getProductId()==productsList.get(i).getProductId()){
                        productsList.get(i).setSelectedProduct(true);
                    }
                }

            }
        }
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

    private  void BindSearchProductTextBox(){
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

    private void BindProductsList(){

        listView = (ListView) findViewById(R.id.lstProducts);
        adapter = new ProductListAdapter(this, productsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EntityProduct entry= (EntityProduct) parent.getAdapter().getItem(position);
                /*Toast.makeText(ProductActivity.this, entry.getName(), Toast.LENGTH_SHORT).show();*/

                Intent returnIntent = new Intent();

                returnIntent.putExtra("productId", String.valueOf(entry.getProductId()));

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        adapter.notifyDataSetChanged();
    }
}
