package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Adapters.ViewPagerAdapter;
import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityCustomer;
import com.ags.agssalesandroidclientorderdocter.Models.EntityOrder;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.Models.EntitySalesman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;

import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Adapters.ProductDetailsListAdapter;
import com.ags.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListener2;
import com.ags.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.ags.agssalesandroidclientorderdocter.fragments.HistoryFragment;
import com.ags.agssalesandroidclientorderdocter.fragments.OrderBooking;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import org.simpleframework.xml.Order;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
            myToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
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
            utils.alertBox(this, "Alert", "Something went wrong", "Ok", new setOnitemClickListner() {
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