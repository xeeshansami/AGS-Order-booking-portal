package com.agsadil.agssalesandroidclientorderdocter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.ProductListAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityCustomer;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityOrder;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntitySalesman;
import com.agsadil.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.agsadil.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.agsadil.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.OnConnectionCallback;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.agsadil.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.agsadil.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductOfferActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    SessionManager sessionManager;
    RecyclerView product_offer_recycler_view;
    private ProductListAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private Location currentLocation;
    private LocationCallback locationCallback;
    EntityCustomer selectedCustomer;
    private static final int REQUEST_CHECK_SETTINGS = 3;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_offer);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        sessionManager = new SessionManager(this);
        boolean isDarkMode = sessionManager.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        product_offer_recycler_view = findViewById(R.id.product_offer_recycler_view);
        myToolbar.setSubtitle("Product Offers");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        downloadMasterData();
        BindProductsList();
    }
    public void downloadMasterData() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    BindProductsList();
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.alertBox(ProductOfferActivity.this, "Internet Connections", "Poor connection, please check your internet connection", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            utils.alertBox(this, "Internet Connections", "Network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    view.dismiss();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                    view.dismiss();
                }
            });
        }
    }
    private void BindProductsList() {
//        utils.showLoader(this);
//        AGSStore.getInstance().getProductOffers(sp.getbranch(), new callback() {
//            @Override
//            public void Success(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.indexOf("}]") + 2));
//                    //TODO: PRODUCTS
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jObject = jsonArray.getJSONObject(i);
//                        EntityProduct product = new EntityProduct();
//                        product.setProductId(Integer.parseInt(jObject.get("prod_id").toString()));
//                        product.setProductName(jObject.get("prod_name").toString());
//                        product.setProductSize(jObject.get("prod_size").toString());
//                        product.setProductPrice(Float.parseFloat(jObject.get("prod_tp").toString()));
//                        product.setProductCompany(jObject.get("prod_company").toString());
//                        product.setProd_Group_Name(jObject.get("Prod_Group_Name").toString());
//                        productsList2.add(product);
//                    }
//                    product_offer_recycler_view.setLayoutManager(new LinearLayoutManager(ProductOfferActivity.this));
//                    adapter = new ProductListAdapter(ProductOfferActivity.this, productsList2, new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(EntityProduct product) {
//                            /*Toast.makeText(ProductActivity.this, entry.getName(), Toast.LENGTH_SHORT).show();*/
//                            String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
//                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.US);
//                            try {
//                                Date offerDate = sdf.parse(offerLimit); // Convert string to Date
//                                Date today = new Date(); // Get today's date
//                                if (offerDate.after(today)) { // Check if offer is in the future (upcoming)
//                                    showProductSchemeDialog(product);
//                                }else{
//                                    Intent returnIntent = new Intent();
//                                    returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
//                                    setResult(Activity.RESULT_OK, returnIntent);
//                                    finish();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace(); // Handle parsing error
//                            }
//                        }
//
//                        @Override
//                        public void onItemLongClick(EntityProduct product) {
//                            String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
//                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//                            try {
//                                Date offerDate = sdf.parse(offerLimit); // Convert string to Date
//                                Date today = new Date(); // Get today's date
//                                if (offerDate.after(today)) { // Check if offer is in the future (upcoming)
//                                    showProductSchemeDialog(product);
//                                }else{
//                                    Intent returnIntent = new Intent();
//                                    returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
//                                    setResult(Activity.RESULT_OK, returnIntent);
//                                    finish();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace(); // Handle parsing error
//                            }
//                        }
//                    });
//                    product_offer_recycler_view.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if(utils!=null) {
//                    utils.hideLoader();
//                }
//            }
//
//            @Override
//            public void Failure(ErrorResponse response) {
//                Toast.makeText(ProductOfferActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                utils.hideLoader();
//            }
//        });
        productsList2=db.getFilteredProducts(1);
        product_offer_recycler_view.setLayoutManager(new LinearLayoutManager(ProductOfferActivity.this));
        adapter = new ProductListAdapter(ProductOfferActivity.this, productsList2, new OnItemClickListener() {
            @Override
            public void onItemClick(EntityProduct product) {
                /*Toast.makeText(ProductActivity.this, entry.getName(), Toast.LENGTH_SHORT).show();*/
                String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.US);
                try {
                    Date offerDate = sdf.parse(offerLimit); // Convert string to Date
                    Date today = new Date(); // Get today's date
                    if (offerDate.after(today)) { // Check if offer is in the future (upcoming)
                        showProductSchemeDialog(product);
                    }else{
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle parsing error
                }
            }

            @Override
            public void onItemLongClick(EntityProduct product) {
                String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH);
                try {
                    Date offerDate = sdf.parse(offerLimit); // Convert string to Date
                    Date today = new Date(); // Get today's date
                    if (offerDate.after(today)) { // Check if offer is in the future (upcoming)
                        showProductSchemeDialog(product);
                    }else{
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle parsing error
                }
            }
        });
        product_offer_recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void showProductSchemeDialog(EntityProduct product) {
        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_scheme, null);
        // Get the dialog views
        TextView productNameTextView = dialogView.findViewById(R.id.productName);
        TextView schemLimiteDate = dialogView.findViewById(R.id.schemLimiteDate);
        TextView schemProductPrice = dialogView.findViewById(R.id.schemProductPrice);
        TextView schemProductSize = dialogView.findViewById(R.id.schemProductSize);
        TextView schemeCompany = dialogView.findViewById(R.id.schemeCompany);
        TextView schemGroup = dialogView.findViewById(R.id.schemGroup);
        Button applyButton = dialogView.findViewById(R.id.applyButton);
        ImageView backgroundImage = dialogView.findViewById(R.id.bonusImage);
        Glide.with(this)
                .asGif()  // Explicitly tell Glide to load the image as a GIF
                .load(R.drawable.bonus)  // Replace with your GIF resource or URL
                .into(backgroundImage);
        // Set values in the dialog
        productNameTextView.setText(product.getProductName());
        String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
        // Step 1: Parse the original format
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date offerDate = inputFormat.parse(offerLimit); // Convert string to Date
            String formattedDate = outputFormat.format(offerDate); // Convert Date to "dd-MMM-yyyy" format
            schemLimiteDate.setText(formattedDate); // Set the formatted date to TextView
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing error
        }
        schemProductSize.setText(product.getProductSize());
        schemProductPrice.setText(String.valueOf(product.getProductPrice() + " PKR"));
        schemGroup.setText(String.valueOf("("+product.getProductCompany())+")");
        schemGroup.setTextColor(Color.parseColor("#069319"));  // Set color for discounted price (e.g., pink)
        SpannableString spannableString = new SpannableString(String.valueOf(product.getProd_Group_Name()));
//        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#B0BEC5")), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  // Grey color for original price
        schemeCompany.setText(spannableString);
        schemeCompany.setGravity(Gravity.CENTER);
        schemeCompany.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(false) // Prevent dialog from closing when tapping outside
                .setPositiveButton("Close", (dialog, id) -> dialog.dismiss()) // Close dialog when pressing the Close button
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss()); // Dismiss dialog

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Optional: Handle Apply button logic
        applyButton.setOnClickListener(v -> {
            // Logic to apply the scheme or any further action
            Intent returnIntent = new Intent();
            returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            dialog.dismiss(); // Close the dialog after applying
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    int customerId = Integer.parseInt(data.getStringExtra("customerId"));

                }
                if (resultCode == RESULT_CANCELED) {
                    utils.hideLoader();
                }
            }
            if (requestCode == 2) {
                if (resultCode == RESULT_OK) {
                    final int productId = Integer.parseInt(data.getStringExtra("productId"));
                    final EntityProduct product = db.getProduct(productId);
                    ShowDialogForDetails(product);
                }
                if (resultCode == RESULT_CANCELED) {
                }
            } else if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK)
                getCurrentLocation();
            if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED)
                utils.hideLoader();
//                Toast.makeText(this, "Please enable Location settings...!!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            utils.hideLoader();
            utils.errorBox(this, "GPS enabling please restart the application");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANT_PERMISSION) {
            getCurrentLocation();
        }
    }
    public void getCurrentLocation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(ProductOfferActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProductOfferActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductOfferActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        utils.hideLoader();
                        return;
                    }
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(ProductOfferActivity.this, new OnSuccessListener<Location>() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onSuccess(Location location) {
                                    Log.d("TAG", "onSuccess: getLastLocation");
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        currentLocation = location;
//                                        Snackbar.make(findViewById(android.R.id.content), "andress=" + location.getLatitude() + "," + location.getLongitude(), 5000).show();
                                        saveDataSuccessFullyInDB(location.getLatitude(), location.getLongitude(), "pakistan");
                                        Log.d("TAG", "onSuccess:latitude " + location.getLatitude());
                                        Log.d("TAG", "onSuccess:longitude " + location.getLongitude());
                                    } else {
                                        utils.hideLoader();
                                        Log.d("TAG", "location is null");
                                        buildLocationCallback();
                                    }
                                }
                            });
                } catch (Exception e) {
                    utils.hideLoader();
                    utils.errorBox(ProductOfferActivity.this, e.getMessage());
                }
            }
        }, 3000);
    }
    public void saveDataSuccessFullyInDB(double latitude, double longitude, String address) {
        try {
            if (SaveOrderValidate()) {
                EntityOrder order = new EntityOrder();
                String date="";
                Calendar dateSelected = Calendar.getInstance();
                int mYear = dateSelected.get(Calendar.YEAR);
                int mMonth = dateSelected.get(Calendar.MONTH);
                int mDay = dateSelected.get(Calendar.DAY_OF_MONTH);
                order.setOrderDate(mMonth + 1 + "/" + mDay + "/" + mYear);
                order.setCustomerCode(String.valueOf(selectedCustomer.getCustomerId()));
                if (db.getAllSalesman().size() == 1) {
                    order.setSaleMenCode(String.valueOf(db.getAllSalesman().get(0).getSalesman_Id()));
                }
                order.setNetTotal(txtNetTotal.getText().toString());
                order.setBranch(selectedCustomer.getCustomerBranch());
//                order.setBranch(sp.getbranch());
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
                order.setOrderCustAddress(selectedCustomer.getCustomerAddress());
                order.setorderCreatedOn(DateFormat.getDateTimeInstance().format(new Date()));
                order.setAllProducts(productsList);
                db.CreateOrder(order);
                db.updateSelectedCustomer(selectedCustomer.getCustomerId());
                this.utils.showMessage(ProductOfferActivity.this, "Order created Successfully");
                Intent intent = new Intent(ProductOfferActivity.this, DashboardActivity.class);
                startActivity(intent);
                utils.hideLoader();
                ProductOfferActivity.this.finish();
            } else {
                utils.hideLoader();
                this.utils.showMessage(ProductOfferActivity.this, "Select customer or add atleast 1 product");
            }
        } catch (Exception e) {
            utils.alertBox(ProductOfferActivity.this, "Error", e.getMessage(), "Ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }
    public boolean SaveOrderValidate() {
        if (selectedCustomer != null && productsList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void buildLocationCallback() {
        try {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    try {
                        if (locationResult == null) {
                            utils.hideLoader();
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            currentLocation = location;
//                            saveDataSuccessFullyInDB(location.getLatitude(), location.getLongitude(), "pakistan");
                            Log.d("TAG", "onLocationResult: " + currentLocation.getLatitude());
                        }
                    } catch (Exception e1) {
                        utils.hideLoader();
                        utils.errorBox(ProductOfferActivity.this, e1.getMessage());
                    }
                }

                ;
            };
        } catch (Exception e) {
            utils.hideLoader();
            utils.errorBox(ProductOfferActivity.this, e.getMessage());
        }
    }
    EntityProduct product = null;
    EditText productName = null;
    EditText productQty = null;
    EditText productBonus = null;
    EditText productDiscount = null;

    TextView txtNetTotal;
    EditText txtRemarks;
    private List<EntityProductDetails> productsList = new ArrayList<EntityProductDetails>();
    private List<EntityProduct> productsList2 = new ArrayList<EntityProduct>();
    private ListView listView;
    public void ShowDialogForDetails(final EntityProduct product) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.layout_product_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogButtonStyle);
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
                                        hideKeyboard(ProductOfferActivity.this);
                                    }
                                }, 200);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        hideKeyboard(ProductOfferActivity.this);
                                    }
                                }, 200);
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public EntityProduct addProductToList() {
        EntityProductDetails detailsProd;
        if (!productQty.getText().toString().trim().equals("")) {

            detailsProd = new EntityProductDetails();

            detailsProd.setProductId(product.getProductId());
            detailsProd.setProductName(product.getProductName());
            detailsProd.setProductSize(product.getProductSize());
            detailsProd.setProductPrice(product.getProductPrice());
            detailsProd.setProductPrice(product.getProductPrice());
            detailsProd.setProductSelected(true);

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
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
            lp.height = 180 * productsList.size();
            listView.setLayoutParams(lp);
        }
        return product;
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
}
