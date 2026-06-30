package com.agsadil.agssalesandroidclientorderdocter.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityCustomer;
import com.agsadil.agssalesandroidclientorderdocter.Network.model.response.ErrorResponse;
import com.agsadil.agssalesandroidclientorderdocter.Network.responseHandler.callbacks.callback;
import com.agsadil.agssalesandroidclientorderdocter.Network.store.AGSStore;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.agsadil.agssalesandroidclientorderdocter.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateCustomerProfile extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHandler db;
    private EntityCustomer selectedCustomer;
    private Calendar myCalendar;
    private AGSStore agsStore;
    private Utils utils;
    private SharedPreferenceHandler sp;
    Toolbar toolbar;
    TextView customer_date;
    Button update_btn, btnSelectCustomer, btnPickLocation;
    EditText customer_name, customer_email, customer_contact, customer_cnic, customer_prop, customer_address, customer_licences, customer_contact_person, customer_strn, customer_location, customer_vLong;

    private static final int REQUEST_SELECT_CUSTOMER = 1;
    private static final int REQUEST_PICK_LOCATION = 2;
    // Failsafe so the progress loader can never hang indefinitely.
    private final Handler loaderTimeoutHandler = new Handler(Looper.getMainLooper());
    private Runnable loaderTimeoutRunnable;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        db = new DatabaseHandler(this);
        boolean isDarkMode = sp.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        utils = new Utils(this);
        myCalendar = Calendar.getInstance();
        agsStore = AGSStore.getInstance();

        setContentView(R.layout.activity_update_customer_profile);
        initViews();

        toolbar.setSubtitle("Update Customer Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().hasExtra("customerId")) {
            int customerId = Integer.parseInt(getIntent().getStringExtra("customerId"));
            selectedCustomer = db.getCustomer(customerId);
            populateCustomerData();
            fetchCustomerDetailsFromAPI(String.valueOf(selectedCustomer.getCustomerId()));
        }

        customer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateCustomerProfile.this, dateListener,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateCustomerProfile.this, MapPickerActivity.class);
                intent.putExtra(MapPickerActivity.EXTRA_LAT, customer_location.getText().toString().trim());
                intent.putExtra(MapPickerActivity.EXTRA_LNG, customer_vLong.getText().toString().trim());
                startActivityForResult(intent, REQUEST_PICK_LOCATION);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validation()) {
                        postUpdateProfile();
                    }
                } catch (Exception e) {
                    hideLoaderSafely();
                    Toast.makeText(UpdateCustomerProfile.this,
                            "Unable to update: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Shows the loader and arms a failsafe that dismisses it after the network
     * call timeout window, so a hung request can never leave the loader stuck.
     */
    private void showLoaderSafely() {
        utils.showLoader(this);
        if (loaderTimeoutRunnable != null) {
            loaderTimeoutHandler.removeCallbacks(loaderTimeoutRunnable);
        }
        loaderTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                if (isFinishing() || isDestroyed()) return;
                utils.hideLoader();
                Toast.makeText(UpdateCustomerProfile.this,
                        "Request timed out, please try again.", Toast.LENGTH_LONG).show();
            }
        };
        // A little beyond the OkHttp call timeout (90s) used by the API client.
        loaderTimeoutHandler.postDelayed(loaderTimeoutRunnable, 95000);
    }

    /** Hides the loader and cancels the failsafe timer. */
    private void hideLoaderSafely() {
        if (loaderTimeoutRunnable != null) {
            loaderTimeoutHandler.removeCallbacks(loaderTimeoutRunnable);
            loaderTimeoutRunnable = null;
        }
        utils.hideLoader();
    }

    @Override
    protected void onDestroy() {
        if (loaderTimeoutRunnable != null) {
            loaderTimeoutHandler.removeCallbacks(loaderTimeoutRunnable);
        }
        super.onDestroy();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        customer_date = findViewById(R.id.customer_date);
        update_btn = findViewById(R.id.update_btn);
        btnSelectCustomer = findViewById(R.id.btnSelectCustomer);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        customer_name = findViewById(R.id.customer_name);
        customer_email = findViewById(R.id.customer_email);
        customer_contact = findViewById(R.id.customer_contact);
        customer_cnic = findViewById(R.id.customer_cnic);
        customer_prop = findViewById(R.id.customer_prop);
        customer_address = findViewById(R.id.customer_address);
        customer_licences = findViewById(R.id.customer_licences);
        customer_contact_person = findViewById(R.id.customer_contact_person);
        customer_strn = findViewById(R.id.customer_strn);
        customer_location = findViewById(R.id.customer_location);
        customer_vLong = findViewById(R.id.customer_vLong);
    }

    private void populateCustomerData() {
        if (selectedCustomer != null) {
            btnSelectCustomer.setText(selectedCustomer.getCustomerName() + "\n" + selectedCustomer.getCustomerAddress());
            customer_name.setText(selectedCustomer.getCustomerName());
            customer_address.setText(selectedCustomer.getCustomerAddress());
            customer_licences.setText(selectedCustomer.getCustomerBranch());
        }
    }

    private void fetchCustomerDetailsFromAPI(String customerId) {
        showLoaderSafely();
        agsStore.getSelfCustomer(sp.getbranch(), customerId, new callback() {
            @Override
            public void Success(String response) {
                hideLoaderSafely();
                try {
                    String cleanResponse = response;
                    if (response.contains("[")) {
                        cleanResponse = response.substring(response.indexOf("["), response.lastIndexOf("]") + 1);
                    }
                    JSONArray jsonArray = new JSONArray(cleanResponse);
                    if (jsonArray.length() > 0) {
                        JSONObject customer = jsonArray.getJSONObject(0);
                        // Field names match the customers2 ASMX response, e.g.
                        // {"ACCOUNT_NAME":..,"Account_Address":..,"Account_CNIC":..,"STRN":..,
                        //  "LicenceNumb":..,"LicenceExpiry":..,"CustProp":..,"CustContact":..,
                        //  "Account_LOCATION1":..,"Account_LOCATION2":..}
                        customer_name.setText(customer.optString("ACCOUNT_NAME"));
                        customer_address.setText(customer.optString("Account_Address"));
                        customer_cnic.setText(customer.optString("Account_CNIC"));
                        // Proprietor
                        if (customer.has("CustProp")) {
                            customer_prop.setText(customer.optString("CustProp"));
                        } else if (customer.has("Account_Prop")) {
                            customer_prop.setText(customer.optString("Account_Prop"));
                        }
                        // Contact number
                        if (customer.has("CustContact")) {
                            customer_contact.setText(customer.optString("CustContact"));
                        } else if (customer.has("Account_Contact")) {
                            customer_contact.setText(customer.optString("Account_Contact"));
                        }
                        // Sales tax registration number (STRN)
                        if (customer.has("STRN")) {
                            customer_strn.setText(customer.optString("STRN"));
                        } else if (customer.has("Account_STRN")) {
                            customer_strn.setText(customer.optString("Account_STRN"));
                        }
                        // Drug licence number
                        if (customer.has("LicenceNumb")) {
                            customer_licences.setText(customer.optString("LicenceNumb"));
                        }
                        // Licence expiry / date (server returns e.g. "7/6/2021 12:00:00 AM")
                        if (customer.has("LicenceExpiry") && !TextUtils.isEmpty(customer.optString("LicenceExpiry"))) {
                            String licenceExpiry = customer.optString("LicenceExpiry");
                            if (licenceExpiry.contains(" ")) {
                                licenceExpiry = licenceExpiry.split(" ")[0];
                            }
                            customer_date.setText(licenceExpiry);
                        }
                        // Geo location
                        if (customer.has("Account_LOCATION1")) {
                            customer_location.setText(customer.optString("Account_LOCATION1"));
                        }
                        if (customer.has("Account_LOCATION2")) {
                            customer_vLong.setText(customer.optString("Account_LOCATION2"));
                        }
                        // Email (not always returned by the server)
                        if (customer.has("Account_Email")) customer_email.setText(customer.optString("Account_Email"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                hideLoaderSafely();
                Toast.makeText(UpdateCustomerProfile.this,
                        "Could not load customer details. " +
                                (response != null && response.getMessage() != null ? response.getMessage() : ""),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    public void SelectCustomer(View v) {
        Intent intent = new Intent(this, CustomerActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_CUSTOMER);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        customer_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CUSTOMER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int customerId = Integer.parseInt(data.getStringExtra("customerId"));
                selectedCustomer = db.getCustomer(customerId);
                populateCustomerData();
                fetchCustomerDetailsFromAPI(String.valueOf(selectedCustomer.getCustomerId()));
            }
        } else if (requestCode == REQUEST_PICK_LOCATION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String lat = data.getStringExtra(MapPickerActivity.EXTRA_LAT);
                String lng = data.getStringExtra(MapPickerActivity.EXTRA_LNG);
                if (lat != null) customer_location.setText(lat);
                if (lng != null) customer_vLong.setText(lng);
            }
        }
    }

    private boolean validation() {
        if (selectedCustomer == null) {
            Toast.makeText(this, "Please select a customer first", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(customer_name.getText().toString().trim())) {
            customer_name.requestFocus();
            customer_name.setError("Name should not be empty");
            Toast.makeText(this, "Name should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(customer_address.getText().toString().trim())) {
            customer_address.requestFocus();
            customer_address.setError("Address should not be empty");
            Toast.makeText(this, "Address should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        String email = customer_email.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !isValidEmail(email)) {
            customer_email.requestFocus();
            customer_email.setError("Please enter a valid email address");
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View view) {
    }

    private void postUpdateProfile() {
        showLoaderSafely();

        String name = customer_name.getText().toString().trim();
        String email = customer_email.getText().toString().trim();
        String mobile = customer_contact.getText().toString().trim();
        String customerId = String.valueOf(selectedCustomer.getCustomerId());
        String vLat = customer_location.getText().toString().trim();
        String vLong = customer_vLong.getText().toString().trim();
        String cnic = customer_cnic.getText().toString().trim();
        String prop = customer_prop.getText().toString().trim();
        String address = customer_address.getText().toString().trim();
        String licenceNo = customer_licences.getText().toString().trim();
        String updatedBy = sp.getusername();
        String contactNo = customer_contact_person.getText().toString().trim();
        String licenceDate = customer_date.getText().toString().trim();
        if (licenceDate.equals("Select Date")) licenceDate = "";
        String strn = customer_strn.getText().toString().trim();

        agsStore.postUpdateProfileForCustomer(name, email, mobile, customerId, vLat, vLong, cnic, prop, address, licenceNo, updatedBy, contactNo, licenceDate, strn, new callback() {
            @Override
            public void Success(String response) {
                hideLoaderSafely();
                try {
                    JSONObject objects = new JSONObject(response);
                    String userId = objects.optString("userid", "0");
                    if (!userId.equalsIgnoreCase("0")) {
                        // Server update succeeded -> keep the local SQLite cache
                        // in sync for this customer (matched by customerId).
                        applyLocalCustomerSync();
                        utils.alertBox(UpdateCustomerProfile.this, "Congratulations!",
                                "Customer profile has been updated", "ok",
                                new setOnitemClickListner() {
                                    @Override
                                    public void onClick(DialogInterface view, int i) {
                                        finish();
                                        view.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(UpdateCustomerProfile.this, "Update failed: " + response, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // Non-JSON success response: still treat as success and sync.
                    applyLocalCustomerSync();
                    Toast.makeText(UpdateCustomerProfile.this, "Update successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                hideLoaderSafely();
                Toast.makeText(UpdateCustomerProfile.this,
                        getResources().getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Mirrors the just-saved form values into the local SQLite customer row
     * (matched by customerId) so the cached data matches the server.
     */
    private void applyLocalCustomerSync() {
        if (selectedCustomer == null || db == null) return;
        selectedCustomer.setCustomerName(customer_name.getText().toString().trim());
        selectedCustomer.setCustomerAddress(customer_address.getText().toString().trim());
        selectedCustomer.setAccountCNIC(customer_cnic.getText().toString().trim());
        selectedCustomer.setAccountTaxRation(customer_strn.getText().toString().trim());
        selectedCustomer.setAccountLocation1(customer_location.getText().toString().trim());
        selectedCustomer.setAccountLocation2(customer_vLong.getText().toString().trim());
        db.updateCustomer(selectedCustomer);
    }
}
