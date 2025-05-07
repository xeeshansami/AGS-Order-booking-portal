package com.agsadil.agssalesandroidclientorderdocter.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agsadil.agssalesandroidclientorderdocter.Adapters.ProductAdapter;
import com.agsadil.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.Models.Product;
import com.agsadil.agssalesandroidclientorderdocter.Models.PurchaseHistoryItem;
import com.agsadil.agssalesandroidclientorderdocter.Network.APIClient;
import com.agsadil.agssalesandroidclientorderdocter.Network.APIInterface;
import com.agsadil.agssalesandroidclientorderdocter.Network.IOnConnectionTimeoutListener;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements IOnConnectionTimeoutListener {
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private LinearLayout product_row_header;
    private ProductAdapter adapter;
    private List<Product> products;
    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();

    public HistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        product_row_header = rootView.findViewById(R.id.product_row_header);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = new DatabaseHandler(getActivity());
        fetchHistory();
        productsList = db.getAllProducts();
        EditText searchInput = rootView.findViewById(R.id.searchInput);
        TextView noDataMessage = rootView.findViewById(R.id.noDataMessage);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the adapter when text changes
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
        // Set up the adapter
        adapter = new ProductAdapter(productsList);
        recyclerView.setAdapter(adapter);
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

        return rootView;
    }

    private void fetchHistory() {
        APIInterface consumerAPI = APIClient.getClient(this).create(APIInterface.class);
        Call<ResponseBody> call = consumerAPI.getPurchaseHistory("7", "5968", "1/1/2025", "10/1/2025");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String xmlResponse = response.body().string();

                        // Extract JSON string from XML
                        String json = xmlResponse
                                .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
                                .replaceAll("<string[^>]*>", "")
                                .replace("</string>", "")
                                .replace("&amp;", "&");

                        // Parse JSON array
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PurchaseHistoryItem>>(){}.getType();
                        List<PurchaseHistoryItem> items = gson.fromJson(json, listType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onConnectionTimeout() {


    }
}
