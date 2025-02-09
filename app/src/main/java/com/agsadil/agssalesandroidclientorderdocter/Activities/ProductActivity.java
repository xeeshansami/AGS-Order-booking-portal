package com.agsadil.agssalesandroidclientorderdocter.Activities;

import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;

import com.agsadil.agssalesandroidclientorderdocter.R;

import android.app.Activity;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.ProductListAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.agsadil.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private SessionManager sessionManager;

    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private List<EntityProduct> productsListSP = new ArrayList<EntityProduct>();
    private RecyclerView listView;
    private ProductListAdapter adapter;
    private EditText txtProductSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = new DatabaseHandler(this);
        sessionManager = new SessionManager(this);
        boolean isDarkMode = sessionManager.isDarkMode();
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        productsList = db.getAllProducts();
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

    private void BindSearchProductTextBox() {
        txtProductSearch = (EditText) findViewById(R.id.searchProductList);

        txtProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String getText = txtProductSearch.getText().toString();
                List<EntityProduct> newProductsList = db.getAllProducts(getText);
                if (adapter != null) {
                    adapter.updateList(newProductsList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void BindProductsList() {
        listView = (RecyclerView) findViewById(R.id.lstProducts);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductListAdapter(this, productsList, new OnItemClickListener() {
            @Override
            public void onItemClick(EntityProduct entry) {
                /*Toast.makeText(ProductActivity.this, entry.getName(), Toast.LENGTH_SHORT).show();*/
                Intent returnIntent = new Intent();
                returnIntent.putExtra("productId", String.valueOf(entry.getProductId()));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onItemLongClick(EntityProduct product) {
                String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
        listView.setAdapter(adapter);

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
}
