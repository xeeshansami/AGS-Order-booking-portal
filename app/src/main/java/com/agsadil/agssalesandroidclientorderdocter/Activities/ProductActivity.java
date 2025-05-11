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
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private SessionManager sessionManager;

    private ArrayList<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private List<EntityProduct> productsListSP = new ArrayList<EntityProduct>();
    private RecyclerView listView;
    private ProductListAdapter adapter;
    private EditText txtProductSearch;
    EditText searchProductList;
    RadioGroup filterGroup;
    ProgressBar progressBar;
    ExecutorService executorService;
    Handler mainHandler;
    ImageView searchButton;
    TextView noProductFound;
    private static final long DEBOUNCE_DELAY = 500; // milliseconds
    private Handler debounceHandler = new Handler();
    private Runnable debounceRunnable;

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
        searchProductList = findViewById(R.id.searchProductList);
        searchButton = findViewById(R.id.searchButton);
        filterGroup = findViewById(R.id.filterRadioGroup);
        progressBar = findViewById(R.id.progressBar);
        productsList = db.getAllProducts();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activities window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        BindSearchProductTextBox();
        BindProductsList();
        filters();
    }

    private void performSearch() {
        String searchQuery = searchProductList.getText().toString().trim();
        if (searchQuery.isEmpty()) return; // Avoid unnecessary queries
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            ArrayList<EntityProduct> filteredProducts = db.getFilteredProducts(getSelectedFilter(filterGroup));
            mainHandler.post(() -> {
                if (adapter != null && !searchQuery.isEmpty()) {
                    adapter.updateList(filteredProducts, getSelectedFilter(filterGroup), progressBar, searchQuery);
                } else {
                    adapter.updateList(filteredProducts, getSelectedFilter(filterGroup), progressBar, searchQuery);
                    searchProductList.setError("Please enter product name");
                    searchProductList.requestFocus();
                }
                progressBar.setVisibility(View.GONE);
            });
        });
    }

    private void filters() {
        EditText searchProductList = findViewById(R.id.searchProductList);
        RadioGroup filterGroup = findViewById(R.id.filterRadioGroup);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        searchProductList.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch();
                return true; // Consume the event
            }
            return false;
        });
        searchButton.setOnClickListener(view -> performSearch());


        searchProductList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Cancel any previous search if it exists
                if (debounceRunnable != null) {
                    debounceHandler.removeCallbacks(debounceRunnable);
                }

                // Create a new runnable for the delayed search
                debounceRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchQuery = charSequence.toString();
                        progressBar.setVisibility(View.VISIBLE);
                        executorService.execute(() -> {
                            mainHandler.post(() -> {
                                if (adapter != null) {
                                    adapter.updateList(db.getFilteredProducts(getSelectedFilter(filterGroup)), getSelectedFilter(filterGroup), progressBar, searchQuery);
                                }
                                progressBar.setVisibility(View.GONE);
                            });
                        });
                    }
                };

                // Execute the runnable after a delay (debounce)
                debounceHandler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        filterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedFilterType = getSelectedFilter(filterGroup);
            String searchQuery = searchProductList.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            executorService.execute(() -> {
                mainHandler.post(() -> {
                    if (adapter != null) {
                        adapter.updateList(db.getFilteredProducts(selectedFilterType), getSelectedFilter(filterGroup), progressBar, searchQuery);
                    }
                    progressBar.setVisibility(View.GONE);
                });
            });
        });
    }

    private int getSelectedFilter(RadioGroup filterGroup) {
        int selectedFilterType = 3; // Default: All Products
        switch (filterGroup.getCheckedRadioButtonId()) {
            case R.id.radioUpcoming:
                selectedFilterType = 1;
                break;
            case R.id.radioExpired:
                selectedFilterType = 2;
                break;
            case R.id.radioAll:
                selectedFilterType = 3;
                break;
        }
        return selectedFilterType;
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
                ArrayList<EntityProduct> newProductsList = db.getAllProducts(getText);
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
        noProductFound = (TextView) findViewById(R.id.noProductFound);
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
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.US);
                try {
                    Date offerDate = sdf.parse(offerLimit); // Convert string to Date
                    Date today = new Date(); // Get today's date
                    if (offerDate.after(today)) { // Check if offer is in the future (upcoming)
                        showProductSchemeDialog(product);
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productId", String.valueOf(product.getProductId()));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle parsing error
                }
            }
        }) {
            @Override
            public void onNoProductsFound(int count, String searchQuery) {
                if (count == 0) {
                    listView.setVisibility(View.GONE);
                    noProductFound.setVisibility(View.VISIBLE);
                    noProductFound.setText("There is no product of this name " + searchQuery);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    noProductFound.setVisibility(View.GONE);
                }
            }
        };
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
        TextView schemProductOffer = dialogView.findViewById(R.id.schemProductOffer);
        TextView schemeCompany = dialogView.findViewById(R.id.schemeCompany);
        TextView schemGroup = dialogView.findViewById(R.id.schemGroup);
        TextView schemSalesTax = dialogView.findViewById(R.id.schemSalesTax);
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
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH);
            Date offerDate = inputFormat.parse(offerLimit); // Parse using correct format
            SimpleDateFormat todaysDate = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"); // Adjust format as needed
            String todayDate = todaysDate.format(new Date());
            Date todays = todaysDate.parse(todayDate); // Parse using correct format
            if (offerDate.after(todays)) { // Check if the offer is upcoming
                schemLimiteDate.setText(offerLimit);
            } else {
                schemLimiteDate.setText("--");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing error
        }
        schemSalesTax.setText(String.valueOf(getSalesTax(1,product.getProductPrice(), Float.parseFloat(product.getProd_salestax()),1)));
        schemProductOffer.setText(product.getProd_Offer());
        schemProductSize.setText(product.getProductSize());
        schemProductPrice.setText(String.valueOf(product.getProductPrice() + " PKR"));
        schemGroup.setText(String.valueOf("(" + product.getProductCompany()) + ")");
        schemGroup.setTextColor(Color.parseColor("#069319"));  // Set color for discounted price (e.g., pink)
        schemeCompany.setText(String.valueOf(product.getProd_Group_Name()));
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
    public float getSalesTax(int productQty, float productPrice, float prod_salestax, float productBonus){
        float qtRate = productQty * productPrice ;
        float qtRateSales = productQty + productBonus * prod_salestax ;
        return qtRate+qtRateSales;
    }
}
