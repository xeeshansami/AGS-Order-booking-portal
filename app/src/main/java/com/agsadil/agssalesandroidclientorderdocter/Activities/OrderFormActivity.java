package com.agsadil.agssalesandroidclientorderdocter.Activities;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.ViewPagerAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityCustomer;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntitySalesman;

import android.annotation.SuppressLint;

import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Adapters.ProductDetailsListAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.agsadil.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class OrderFormActivity extends AppCompatActivity {

    Toolbar myToolbar;
    DatabaseHandler db;
    SharedPreferenceHandler sp;
    SessionManager sessionManager;
    /**
     * set Google location listner
     */
    double latitude, longitude;
    Context mContext;
    Calendar dateSelected;
    TextView datePicker, txtSelectSalesman;
    Spinner spinnerSalesMan;
    TextView textViewCustomer, customer_selection_lbl;
    TextView textViewCustomerTown;
    Button btnSelectCustomer;
    TextView txtNetTotal;
    EditText txtRemarks;
    Button btnSetDate;
    private List<EntityProductDetails> productsList = new ArrayList<EntityProductDetails>();
    private ListView listView;
    private ProductDetailsListAdapter adapter;
    private CardView CardViewallSelectedProds;
    private CardView CardViewNetTotal;
    private static final int REQUEST_CHECK_SETTINGS = 3;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;
    EntityCustomer selectedCustomer;
    EntitySalesman selectedSalesMan;
    Utils utils;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_order_form);
            mContext = this;
            utils = new Utils(this);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(OrderFormActivity.this);
            db = new DatabaseHandler(this);
            sp = new SharedPreferenceHandler(this);
            sessionManager = new SessionManager(this);
            boolean isDarkMode = sessionManager.isDarkMode();
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Find the toolbar view inside the activity layout
            myToolbar = (Toolbar) findViewById(R.id.toolbar);
            txtSelectSalesman = findViewById(R.id.txtSelectSalesman);
            myToolbar.setTitle("Order Details");
            myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            setSupportActionBar(myToolbar);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            viewPagerAdapter = new ViewPagerAdapter(this);
            // Initialize views
            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);

            // Setup ViewPagerAdapter

            viewPager.setAdapter(viewPagerAdapter);
            // Setup TabLayout with ViewPager
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Order Booking");
                        break;
                    case 1:
                        tab.setText("History");
                        break;
                }
            }).attach();
        } catch (Exception e) {
            Log.i("Exceptions",e.getMessage());
            utils.alertBox(this, "Alert", "Something went wrong\n"+e.getMessage()+"\n"+e.getStackTrace(), "Ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }




    @Override
    public void onBackPressed() {
        // Check if productsList size is greater than 0
        if (viewPager.getCurrentItem()==0) {
            utils.alertBox(this, "Alert", "Do you want to Cancel this Order?", "Yes", "No", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                }
            });
        }else{
            // Default back press behavior
            super.onBackPressed();
        }
    }

}