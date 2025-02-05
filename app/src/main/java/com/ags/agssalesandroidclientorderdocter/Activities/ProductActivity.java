package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.R;

import android.app.Activity;

import com.ags.agssalesandroidclientorderdocter.Adapters.ProductListAdapter;
import com.ags.agssalesandroidclientorderdocter.Utils.SessionManager;
import com.ags.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

                productsList = db.getAllProducts(getText);
                BindProductsList();

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
                showProductSchemeDialog(product.getProductName(), product.getProductPrice(), product.getProductSize(), product.getProductCompany(), "50%");
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showProductSchemeDialog(String productName, float productPrice, String productSize, String schemeDescription, String discount) {
        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_scheme, null);

        // Get the dialog views
        TextView productNameTextView = dialogView.findViewById(R.id.productName);
        TextView schemeDescriptionTextView = dialogView.findViewById(R.id.schemDesction);
        TextView schemeDiscountTextView = dialogView.findViewById(R.id.schemDiscpount);
        TextView schemProductPrice = dialogView.findViewById(R.id.schemProductPrice);
        TextView schemProductSize = dialogView.findViewById(R.id.schemProductSize);

        Button applyButton = dialogView.findViewById(R.id.applyButton);
        // Set values in the dialog
        productNameTextView.setText(productName);
        schemeDescriptionTextView.setText(schemeDescription);
        schemProductSize.setText(productSize);
        schemProductPrice.setText(String.valueOf(productPrice + " PKR"));
        schemeDiscountTextView.setText(discount);

        TextView schemAfterDiscpount = dialogView.findViewById(R.id.schemAfterDiscpount);
        // Assuming the original price and the discounted price are already calculated
        double originalPrice = Float.parseFloat(String.valueOf(productPrice));
        double discountCalculation = originalPrice * 0.5;  // 50% discount
        // Set the discounted price (change color for discounted price)
        schemAfterDiscpount.setText(String.valueOf(discountCalculation)+" PKR");
        schemAfterDiscpount.setTextColor(Color.parseColor("#069319"));  // Set color for discounted price (e.g., pink)
        // Create a SpannableString to apply a strikethrough to the original price and change color
        SpannableString spannableString = new SpannableString(String.valueOf(originalPrice));
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set color for the original price (crossed out)
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#B0BEC5")), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  // Grey color for original price
        // Assuming you have a TextView for displaying the original price (crossed out)
        TextView originalPriceTextView = dialogView.findViewById(R.id.originalPriceTextView);
        // Set the strikethrough price
        originalPriceTextView.setText(spannableString);
        // Center the text inside the TextViews
        originalPriceTextView.setGravity(Gravity.CENTER);
        schemAfterDiscpount.setGravity(Gravity.CENTER);
        // Optional: you can also apply other text styles (e.g., bold) for both prices if needed.
        // Create and show the dialog
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
            dialog.dismiss(); // Close the dialog after applying
        });
    }
}
