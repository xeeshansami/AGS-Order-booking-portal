package com.ags.agssalesandroidclientorder.Activities;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

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
    Button update_btn,btnSelectCustomer;
    EditText customer_address,customer_licences,customer_location,customer_contact_person;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer_profile);
        toolbar=findViewById(R.id.toolbar);
        customer_date=findViewById(R.id.customer_date);
        update_btn=findViewById(R.id.update_btn);
        btnSelectCustomer=findViewById(R.id.btnSelectCustomer);
        customer_address=findViewById(R.id.customer_address);
        customer_licences=findViewById(R.id.customer_licences);
        customer_location=findViewById(R.id.customer_location);
        customer_contact_person=findViewById(R.id.customer_contact_person);
        toolbar.setSubtitle("Update Customer Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp = new SharedPreferenceHandler(this);
        db = new DatabaseHandler(this);
        utils = new Utils(this);
        myCalendar = Calendar.getInstance();
        agsStore = AGSStore.getInstance();

        customer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(UpdateCustomerProfile.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    postUpdateProfile(btnSelectCustomer.getText().toString().trim(),
                            customer_address.getText().toString().trim(),
                            customer_licences.getText().toString().trim(),
                            customer_date.getText().toString().trim(),
                            customer_location.getText().toString().trim(),
                            customer_location.getText().toString().trim(),
                            customer_contact_person.getText().toString().trim(),
                            sp.getuserid());
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.add(Calendar.DATE, 0);
            updateLabel();
        }
    };

    public void SelectCustomer(View v) {
        Intent intent = new Intent(this, CustomerActivity.class);
        startActivityForResult(intent, 1);
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        customer_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedCustomer = new EntityCustomer();
                int customerId = Integer.parseInt(data.getStringExtra("customerId"));
                selectedCustomer = db.getCustomer(customerId);
                btnSelectCustomer.setText(selectedCustomer.getCustomerName() + "\n" + selectedCustomer.getCustomerAddress());
                customer_address.setText(selectedCustomer.getCustomerAddress());
                customer_licences.setText(selectedCustomer.getCustomerBranch());
            }
        }
    }

    private boolean validation() {
        String name = btnSelectCustomer.getText().toString().trim();
        String address = customer_address.getText().toString().trim();
        String licence = customer_licences.getText().toString().trim();
        String contact = customer_contact_person.getText().toString().trim();

        if (name.equalsIgnoreCase("Select Customer")) {
            btnSelectCustomer.requestFocus();
            btnSelectCustomer.setError("Please select a customer");
            Snackbar.make(findViewById(android.R.id.content), "Please select a customer", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            customer_address.requestFocus();
            customer_address.setError("Address should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Address should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(licence)) {
            customer_licences.requestFocus();
            customer_licences.setError("Licence should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Licence should not be empty", 1000).show();
            return false;
        } else if (licence.length() < 4) {
            customer_licences.requestFocus();
            customer_licences.setError("Licence number should be at least 4 numbers");
            Snackbar.make(findViewById(android.R.id.content), "Licence number should be at least 4 numbers", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(contact)) {
            customer_contact_person.requestFocus();
            customer_contact_person.setError("Contact should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Contact should not be empty", 1000).show();
            return false;
        } else if (contact.length() < 11) {
            customer_contact_person.requestFocus();
            customer_contact_person.setError("Contact number should be at least 11 numbers");
            Snackbar.make(findViewById(android.R.id.content), "Contact number should be at least 11 numbers", 1000).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_btn:
                break;
        }
    }

    private void postUpdateProfile(String customerName, String customerAddress, String customerLicence,
                                   String customerDate, String vLat, String vLong,
                                   String customerContactPerson, String userId) {
        utils.showLoader(this);
        agsStore.postUpdateProfileForCustomer(customerName, vLat, vLong, customerLicence,
                customerDate, customerContactPerson, customerAddress, userId, new callback() {
                    @Override
                    public void Success(String response) {
                        utils.hideLoader();
                        try {
                            JSONObject objects = new JSONObject(response);
                            String userid = objects.getString("userid");
                            if (!userid.equalsIgnoreCase("0")) {
                                utils.alertBox(UpdateCustomerProfile.this, "Congratulations!",
                                        "Your profile has been updated", "ok",
                                        new setOnitemClickListner() {
                                            @Override
                                            public void onClick(DialogInterface view, int i) {
                                                onBackPressed();
                                                view.dismiss();
                                            }
                                        });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failure(ErrorResponse response) {
                        Toast.makeText(UpdateCustomerProfile.this,
                                getResources().getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT).show();
                        utils.hideLoader();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
