package com.agsadil.agssalesandroidclientorderdocter.Activities;

import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityCustomer;

import com.agsadil.agssalesandroidclientorderdocter.R;

import android.app.Activity;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.CustomerListAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.agsadil.agssalesandroidclientorderdocter.interfaces.OnItemClickListenerCustomer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar ab;

    private List<EntityCustomer> customersList = new ArrayList<EntityCustomer>();
    private DatabaseHandler db;
    private SessionManager sessionManager;
    private RecyclerView listView;
    private CustomerListAdapter adapter;
    private EditText txtCustomerSearch;
    int customerID=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        db = new DatabaseHandler(this);
        sessionManager = new SessionManager(this);
        boolean isDarkMode = sessionManager.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        customersList = db.getAllCustomers();
        if (getIntent().hasExtra("selectedCustomer")) {
            EntityCustomer obj = (EntityCustomer) getIntent().getSerializableExtra("selectedCustomer");
            customerID=obj.getCustomerId();
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

        listView = (RecyclerView) findViewById(R.id.lstCustomers);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerListAdapter(this, customerID,customersList, new OnItemClickListenerCustomer() {
            @Override
            public void onItemClick(EntityCustomer entry) {
                Intent returnIntentToOrderForm = new Intent();
                returnIntentToOrderForm.putExtra("customerId", String.valueOf(entry.getCustomerId()));
                setResult(Activity.RESULT_OK, returnIntentToOrderForm);
                finish();
            }
        });
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
