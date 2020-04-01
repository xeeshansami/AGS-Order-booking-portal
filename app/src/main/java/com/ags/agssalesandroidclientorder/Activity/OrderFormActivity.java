package com.ags.agssalesandroidclientorder.Activity;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;

import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.classes.ProductDetailsListAdapter;
import com.ags.agssalesandroidclientorder.classes.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.utils.myLogs;

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
import android.provider.Settings;
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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

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
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class OrderFormActivity extends AppCompatActivity {

    DatabaseHandler db;
    SharedPreferenceHandler sp;
    /**
     * set Google location listner
     */
    double latitude, longitude;
    Context mContext;
    Calendar dateSelected;
    TextView datePicker;
    Spinner spinnerSalesMan;
    TextView textViewCustomer;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {

                    int customerId = Integer.parseInt(data.getStringExtra("customerId"));

                    selectedCustomer = db.getCustomer(customerId);

                    textViewCustomer.setText(selectedCustomer.getCustomerName());
                    textViewCustomer.setVisibility(View.VISIBLE);

                    textViewCustomerTown.setText(selectedCustomer.getCustomerBranch());
                    textViewCustomerTown.setVisibility(View.VISIBLE);

                    btnSelectCustomer.setText("Change Customer");


                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "i am called in canncelled", Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == 2) {

                if (resultCode == Activity.RESULT_OK) {

                    final int productId = Integer.parseInt(data.getStringExtra("productId"));
                    final EntityProduct product = db.getProduct(productId);

                    ShowDialogForDetails(product);


                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }
            } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK)
                getCurrentLocation();
            if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Please enable Location settings...!!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            myLogs.errorBox(this,"GPS enabling please restart the application");
        }
    }

    //called after user responds to location permission popup
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANT_PERMISSION) {
            getCurrentLocation();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_order_form);
            mContext = this;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(OrderFormActivity.this);
            db = new DatabaseHandler(this);
            sp = new SharedPreferenceHandler(this);
            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            // Sets the Toolbar to act as the ActionBar for this Activity window.
            // Make sure the toolbar exists in the activity and is not null
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Order Form");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            dateSelected = Calendar.getInstance();
            datePicker = (TextView) findViewById(R.id.txtDatePicker);

            int mYear = dateSelected.get(Calendar.YEAR);
            int mMonth = dateSelected.get(Calendar.MONTH);
            int mDay = dateSelected.get(Calendar.DAY_OF_MONTH);

            datePicker.setText(mMonth + 1 + "/" + mDay + "/" + mYear);


            textViewCustomer = (TextView) findViewById(R.id.txtSelectCustomer);
            textViewCustomerTown = (TextView) findViewById(R.id.txtSelectCustomerTown);

            btnSelectCustomer = (Button) findViewById(R.id.btnSelectCustomer);
            txtNetTotal = (TextView) findViewById(R.id.txtNetTotal);

            btnSetDate = (Button) findViewById(R.id.btnSetDate);

            CardViewallSelectedProds = (CardView) findViewById(R.id.CardViewallSelectedProds);
            CardViewallSelectedProds.setVisibility(View.GONE);

            CardViewNetTotal = (CardView) findViewById(R.id.CardViewNetTotal);
            CardViewNetTotal.setVisibility(View.GONE);

            listView = (ListView) findViewById(R.id.lstViewSelectedProducts);
            adapter = new ProductDetailsListAdapter(this, productsList);
            listView.setAdapter(adapter);

            BindListViewForEdit();

            txtRemarks = (EditText) findViewById(R.id.editTextRemarks);

            BindSalesManSpinner();
            BindCustomer();
        } catch (Exception e) {
            myLogs.errorBox(this,e.getMessage());
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }




    protected void BindListViewForEdit() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityProductDetails entry = (EntityProductDetails) parent.getAdapter().getItem(position);
                ShowDialogForDetails(entry, position);
            }
        });
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


    public void SaveOrder(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to save this order?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*Save data in db*/
                dialog.dismiss();

                createLocationRequest();
                settingsCheck();
                if (ActivityCompat.checkSelfPermission(OrderFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GRANT_PERMISSION);
                    return;
                }
                if (locationCallback == null)
                    buildLocationCallback();
                if (currentLocation == null)
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void createLocationRequest() {
        try {
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } catch (Exception e1) {
            myLogs.errorBox(this,e1.getMessage());
        }
    }

    // Check for location settings
    public void settingsCheck() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                Log.d("TAG", "onSuccess: settingsCheck");
                try {
                    getCurrentLocation();
                } catch (Exception e1) {
                    myLogs.errorBox(OrderFormActivity.this,e1.getMessage());
                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        Log.d("TAG", "onFailure: settingsCheck");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(OrderFormActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        } catch (Exception e1) {
                            myLogs.errorBox(OrderFormActivity.this,e1.getMessage());
                        }
                    }
                } catch (Exception e1) {
                    myLogs.errorBox(OrderFormActivity.this,e1.getMessage());
                }
            }
        });
    }

    public void getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("TAG", "onSuccess: getLastLocation");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentLocation = location;
                                Snackbar.make(findViewById(android.R.id.content),"andress="+location.getLatitude()+","+location.getLongitude(),5000).show();
                                saveDataSuccessFullyInDB(location.getLatitude(), location.getLongitude(), "pakistan");
                                Log.d("TAG", "onSuccess:latitude " + location.getLatitude());
                                Log.d("TAG", "onSuccess:longitude " + location.getLongitude());
                            } else {
                                Log.d("TAG", "location is null");
                                buildLocationCallback();
                            }
                        }
                    });
        } catch (Exception e) {
            myLogs.errorBox(this,e.getMessage());
        }
    }

    private void buildLocationCallback() {
        try {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    try {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            currentLocation = location;
//                            saveDataSuccessFullyInDB(location.getLatitude(), location.getLongitude(), "pakistan");
                            Log.d("TAG", "onLocationResult: " + currentLocation.getLatitude());
                        }
                    } catch (Exception e1) {
                        myLogs.errorBox(OrderFormActivity.this,e1.getMessage());
                    }
                }

                ;
            };
        } catch (Exception e) {
            myLogs.errorBox(this,e.getMessage());
        }
    }

    public void saveDataSuccessFullyInDB(double latitude, double longitude, String address) {
        if (SaveOrderValidate()) {
            EntityOrder order = new EntityOrder();
            order.setOrderDate(datePicker.getText().toString());
            order.setCustomerCode(String.valueOf(selectedCustomer.getCustomerId()));
            order.setSaleMenCode(spinnerSalesMan.getSelectedItem().toString().substring(1, spinnerSalesMan.getSelectedItem().toString().indexOf("]")));
            order.setNetTotal(txtNetTotal.getText().toString());
            order.setBranch(sp.getbranch());
            order.setRemarks(txtRemarks.getText().toString());
            order.setLocation(String.valueOf(latitude));
            order.setLocation1(String.valueOf(longitude));
            order.setOrderAddress(address);
            // old saleman logic below
            ///order.setOrderSalName(spinnerSalesMan.getSelectedItem().toString().substring(spinnerSalesMan.getSelectedItem().toString().indexOf("]") + 2));

            // new saleman logic as defined in ticket Task 15
            EntitySalesman salesManEntity = db.getSalesMan(Integer.parseInt(order.getSaleMenCode()));
            order.setOrderSalName(salesManEntity.getSalesman_Name());

            order.setOrderCustName(selectedCustomer.getCustomerName());
            order.setOrderCustAddress(selectedCustomer.getCustomerBranch());
            order.setorderCreatedOn(DateFormat.getDateTimeInstance().format(new Date()));
            order.setAllProducts(productsList);
            db.CreateOrder(order);

            Toast.makeText(OrderFormActivity.this, "Order created Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(OrderFormActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(OrderFormActivity.this, "Select customer or add atleast 1 product", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean SaveOrderValidate() {
        if (selectedCustomer != null && productsList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void ResetOrder(MenuItem item) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to reset this order?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                if (sp.getrole().equals("Customer")) {
                    txtRemarks.setText("");
                } else {
                    selectedCustomer = null;

                    textViewCustomer.setText("");
                    textViewCustomer.setVisibility(View.GONE);

                    textViewCustomerTown.setText("");
                    textViewCustomerTown.setVisibility(View.GONE);

                    btnSelectCustomer.setText("Select Customer");
                }


                txtRemarks.setText("");
                productsList = new ArrayList<EntityProductDetails>();
                adapter.notifyDataSetChanged();

                CardViewallSelectedProds.setVisibility(View.GONE);
                CardViewNetTotal.setVisibility(View.GONE);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setDateTimeField(View v) {

        int mYear = dateSelected.get(Calendar.YEAR);
        int mMonth = dateSelected.get(Calendar.MONTH);
        int mDay = dateSelected.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                new mDateSetListener(), mYear, mMonth, mDay);

        dialog.show();

    }

    protected void BindSalesManSpinner() {
        try {
            spinnerSalesMan = (Spinner) findViewById(R.id.spinnerSelectSalesMan);

            List<EntitySalesman> salesman;

            if (sp.getrole().equals("Saleman")) {
                spinnerSalesMan.setEnabled(false);
                spinnerSalesMan.setAlpha(0.4f);

                salesman = new ArrayList<EntitySalesman>();

                EntitySalesman saleman1 = new EntitySalesman();
                saleman1.setSalesman_Id(Integer.parseInt(sp.getcategory()));
                saleman1.setSalesman_Name(sp.getusername());

                salesman.add(saleman1);
            } else {
                salesman = db.getAllSalesman();
            }

            // Spinner Drop down elements

            List<String> allSalesMan = new ArrayList<String>();


            for (EntitySalesman eachSaleMan : salesman) {

                allSalesMan.add("[" + eachSaleMan.getSalesman_Id() + "] " + eachSaleMan.getSalesman_Name());

            }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allSalesMan);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinnerSalesMan.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    protected void BindCustomer() {
        if (sp.getrole().equals("Customer")) {

            selectedCustomer = new EntityCustomer();

            selectedCustomer.setCustomerId(Integer.parseInt(sp.getcategory()));
            selectedCustomer.setCustomerName(sp.getUser_Category());
            selectedCustomer.setCustomerBranch(sp.getbranch());

            EntityCustomer queryCustomer = db.getCustomer(selectedCustomer.getCustomerId());

            textViewCustomer.setText(selectedCustomer.getCustomerName());
            textViewCustomer.setVisibility(View.VISIBLE);

            textViewCustomerTown.setText(queryCustomer.getCustomerBranch());
            textViewCustomerTown.setVisibility(View.VISIBLE);

            btnSelectCustomer.setVisibility(View.GONE);

        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void SelectCustomer(View v) {
        Intent intent = new Intent(OrderFormActivity.this, CustomerActivity.class);
        startActivityForResult(intent, 1);
    }

    public void SelectProduct(View v) {
        Intent intent = new Intent(OrderFormActivity.this, ProductActivity.class);
        startActivityForResult(intent, 2);
    }

    EditText productName = null;
    EditText productQty = null;
    EditText productBonus = null;
    EditText productDiscount = null;

    EntityProduct product = null;

    public void ShowDialogForDetails(final EntityProduct product) {
        LayoutInflater li = LayoutInflater.from(OrderFormActivity.this);
        View promptsView = li.inflate(R.layout.layout_product_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderFormActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        this.product = product;

        final EditText productName = (EditText) promptsView.findViewById(R.id.productName);
        productQty = (EditText) promptsView.findViewById(R.id.productQty);
        new Handler().postDelayed(new Runnable() {

            public void run() {
//        ((EditText) findViewById(R.id.et_find)).requestFocus();
//
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);

                productQty.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                productQty.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
        productQty.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(promptsView, InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(productQty, InputMethodManager.SHOW_IMPLICIT);
        productBonus = (EditText) promptsView.findViewById(R.id.productBonus);
        productDiscount = (EditText) promptsView.findViewById(R.id.productDiscount);

        productName.setText(product.getProductName());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                addProductToList();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        hideKeyboard(OrderFormActivity.this);
                                    }
                                }, 200);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        hideKeyboard(OrderFormActivity.this);
                                    }
                                }, 200);
                                dialog.cancel();
                            }
                        })
                .setNeutralButton("Save & Add More",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                addProductToList();
                                Intent intent = new Intent(OrderFormActivity.this, ProductActivity.class);
                                startActivityForResult(intent, 2);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        hideKeyboard(OrderFormActivity.this);
                                    }
                                }, 200);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addProductToList() {
        EntityProductDetails detailsProd;
        if (!productQty.getText().toString().trim().equals("")) {

            detailsProd = new EntityProductDetails();

            detailsProd.setProductId(product.getProductId());
            detailsProd.setProductName(product.getProductName());
            detailsProd.setProductSize(product.getProductSize());
            detailsProd.setProductPrice(product.getProductPrice());

            detailsProd.setProductQty(Integer.parseInt(productQty.getText().toString()));
            detailsProd.setProductBonus(productBonus.getText().toString().trim().equals("") != true ? Integer.parseInt(productBonus.getText().toString()) : 0);
            detailsProd.setProductDiscount(productDiscount.getText().toString().trim().equals("") != true ? Integer.parseInt(productDiscount.getText().toString()) : 0);
//            detailsProd.setLatitude(String.valueOf(latitude));
//            detailsProd.setLongitude(String.valueOf(longitude));
//            detailsProd.setProductEnrtyFromAddress(address);
            productsList.add(detailsProd);
            float oldValue = Float.parseFloat(txtNetTotal.getText().toString());
            txtNetTotal.setText(String.valueOf(oldValue + detailsProd.getItemValue()));

            adapter.notifyDataSetChanged();

            CardViewallSelectedProds.setVisibility(View.VISIBLE);
            CardViewNetTotal.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
            lp.height = 180 * productsList.size();
            listView.setLayoutParams(lp);
        }
    }

    public void ShowDialogForDetails(final EntityProductDetails product, final int position) {
        LayoutInflater li = LayoutInflater.from(OrderFormActivity.this);
        View promptsView = li.inflate(R.layout.layout_product_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderFormActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText productQty = (EditText) promptsView.findViewById(R.id.productQty);
        new Handler().postDelayed(new Runnable() {

            public void run() {
//        ((EditText) findViewById(R.id.et_find)).requestFocus();
//
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);

                productQty.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                productQty.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
        productQty.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(promptsView, InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(productQty, InputMethodManager.SHOW_IMPLICIT);
        final EditText productBonus = (EditText) promptsView.findViewById(R.id.productBonus);
        final EditText productDiscount = (EditText) promptsView.findViewById(R.id.productDiscount);

        productQty.setText(String.valueOf(product.getProductQty()));
        productBonus.setText(String.valueOf(product.getProductBonus()));
        productDiscount.setText(String.valueOf(product.getProductDiscount()));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text

                                if (!productQty.getText().toString().trim().equals("")) {

                                    product.setProductQty(Integer.parseInt(productQty.getText().toString()));
                                    product.setProductBonus(productBonus.getText().toString().trim().equals("") != true ? Integer.parseInt(productBonus.getText().toString()) : 0);
                                    product.setProductDiscount(productDiscount.getText().toString().trim().equals("") != true ? Integer.parseInt(productDiscount.getText().toString()) : 0);

                                    productsList.add(product);

                                    productsList.remove(position);

                                    adapter.notifyDataSetChanged();

                                    txtNetTotal.setText(String.valueOf(sum(productsList)));

                                    CardViewallSelectedProds.setVisibility(View.VISIBLE);
                                    CardViewNetTotal.setVisibility(View.VISIBLE);

                                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
                                    lp.height = 180 * productsList.size();
                                    listView.setLayoutParams(lp);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNeutralButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                productsList.remove(position);
                                adapter.notifyDataSetChanged();

                                txtNetTotal.setText(String.valueOf(sum(productsList)));

                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
                                lp.height = 180 * productsList.size();
                                listView.setLayoutParams(lp);

                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            datePicker.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/").append(mDay).append("/")
                    .append(mYear).append(" "));
        }

    }

    public float sum(List<EntityProductDetails> allProds) {

        float sum = 0;

        for (EntityProductDetails eachDet : allProds) {

            sum += eachDet.getItemValue();

        }

        return sum;

    }
}