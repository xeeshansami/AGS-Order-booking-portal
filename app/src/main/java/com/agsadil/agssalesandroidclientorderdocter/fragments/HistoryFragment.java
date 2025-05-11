package com.agsadil.agssalesandroidclientorderdocter.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.ProductAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.Models.Product;
import com.agsadil.agssalesandroidclientorderdocter.Models.PurchaseHistoryItem;
import com.agsadil.agssalesandroidclientorderdocter.Network.APIConstants;
import com.agsadil.agssalesandroidclientorderdocter.Network.IOnConnectionTimeoutListener;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.agsadil.agssalesandroidclientorderdocter.Utils.SharedViewModel;
import com.agsadil.agssalesandroidclientorderdocter.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HistoryFragment extends Fragment implements IOnConnectionTimeoutListener {
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private LinearLayout product_row_header;
    private ProductAdapter adapter;
    private List<Product> products;
    Utils utils;
    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private boolean hasFetched = false;
    private String lastCustomerId = null;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasFetched && lastCustomerId != null) {
            fetchHistory(sharedPreferenceManager.getbranch(), lastCustomerId, getView());
            hasFetched = true;
        }
    }

    SharedPreferenceHandler sharedPreferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        product_row_header = rootView.findViewById(R.id.product_row_header);
        utils = new Utils(getActivity());
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPreferenceManager = new SharedPreferenceHandler(getActivity());
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSharedValue().observe(getViewLifecycleOwner(), customerID -> {
            lastCustomerId = customerID;
            hasFetched = false; // reset so it fetches again if tab opened
        });
        return rootView;
    }

    private void fetchHistory(String branch, String customerID, View rootView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        // UI references


        // Show loader before starting background task
        utils.showLoader(getActivity());

        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "branch=" + branch + "&CustomerId=" + customerID);
            Request request = new Request.Builder()
                    .url("https://mobile.agssukkur.com/agssalesclient.asmx/CustomerPurchaseHistoryQuery")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String xmlResponse = response.body().string();

                    String json = xmlResponse
                            .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
                            .replaceAll("<string[^>]*>", "")
                            .replace("</string>", "")
                            .replace("&amp;", "&");

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PurchaseHistoryItem>>() {
                    }.getType();
                    List<PurchaseHistoryItem> items = gson.fromJson(json, listType);
                    for (PurchaseHistoryItem x : items) {
                        Log.i("productHistory", x.rate);
                        Log.i("productHistory", x.qty);
                        Log.i("productHistory", x.itemName);
                        Log.i("productHistory", x.itemCode);
                        Log.i("productHistory", x.bonus);
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            EditText searchInput = rootView.findViewById(R.id.searchInput);
                            TextView noDataMessage = rootView.findViewById(R.id.noDataMessage);
                            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView); // Make sure it's initialized
                            View product_row_header = rootView.findViewById(R.id.product_row_header);
                            adapter = new ProductAdapter(items);
                            recyclerView.setAdapter(adapter);
                            if (items.size() != 0) {
                                searchInput.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                noDataMessage.setVisibility(View.GONE);

                            } else {
                                searchInput.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                noDataMessage.setVisibility(View.VISIBLE);
                            }
                            searchInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adapter.filter(s.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            adapter.setOnDataChangedListener(isEmpty -> {
                                if (isEmpty) {
                                    product_row_header.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    noDataMessage.setVisibility(View.VISIBLE);
                                } else {
                                    product_row_header.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noDataMessage.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                } else {
                    Log.e("HTTP_ERROR", "Request failed. Code: " + response.code());
                }
                utils.hideLoader();
            } catch (Exception e) {
                utils.hideLoader();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onConnectionTimeout() {

    }
}
