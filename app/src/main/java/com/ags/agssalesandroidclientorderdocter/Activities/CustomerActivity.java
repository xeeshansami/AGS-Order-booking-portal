package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityCustomer;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.R;

import android.app.Activity;

import com.ags.agssalesandroidclientorderdocter.Adapters.CustomerListAdapter;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar ab;

    private List<EntityCustomer> customersList = new ArrayList<EntityCustomer>();
    private DatabaseHandler db;
    private ListView listView;
    private CustomerListAdapter adapter;
    private EditText txtCustomerSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        db = new DatabaseHandler(this);
        customersList = db.getAllCustomers();
        if (getIntent().hasExtra("selectedCustomer")) {
            EntityCustomer obj = (EntityCustomer) getIntent().getSerializableExtra("selectedCustomer");
            for(int i=0;i<customersList.size();i++){
                if(obj.getCustomerId()==customersList.get(i).getCustomerId()){
                    customersList.get(i).setSelectedCustomer(true);
                    Log.i("CustomerProd",obj.getCustomerId()+" checked "+customersList.get(i).getCustomerId());
                }
            }
        }
        SetToolBar();
        BindSearchCustomerTextBox();
        BindCustomersList();

    }

    private void SetToolBar() {

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Sets the Toolbar to act as the ActionBar for this Activities window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        ab = getSupportActionBar();

        ab.setTitle("Select Customer");
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void BindSearchCustomerTextBox() {
        txtCustomerSearch = (EditText) findViewById(R.id.searchCustomerList);

        txtCustomerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getText = txtCustomerSearch.getText().toString();
                customersList = db.getAllCustomers(getText);
                BindCustomersList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void BindCustomersList() {

        listView = (ListView) findViewById(R.id.lstCustomers);
        adapter = new CustomerListAdapter(this, customersList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityCustomer entry = (EntityCustomer) parent.getAdapter().getItem(position);
                Intent returnIntentToOrderForm = new Intent();
                returnIntentToOrderForm.putExtra("customerId", String.valueOf(entry.getCustomerId()));
                setResult(Activity.RESULT_OK, returnIntentToOrderForm);
                finish();
            }
        });

        adapter.notifyDataSetChanged();
    }
}
