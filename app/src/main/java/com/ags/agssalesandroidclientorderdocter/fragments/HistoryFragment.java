package com.ags.agssalesandroidclientorderdocter.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ags.agssalesandroidclientorderdocter.Adapters.ProductAdapter;
import com.ags.agssalesandroidclientorderdocter.Models.Product;
import com.ags.agssalesandroidclientorderdocter.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data for products
        products = new ArrayList<>();
        products.add(new Product("Product 1", 10, 15.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 2", 5, 30.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 1", 10, 15.00));
        products.add(new Product("Product 1", 10, 15.00));
        products.add(new Product("Product 1", 10, 15.00));
        products.add(new Product("Product 2", 5, 30.00));
        products.add(new Product("Product 2", 5, 30.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 2", 5, 30.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        products.add(new Product("Product 3", 12, 25.00));
        // Add more products as needed...

        // Set up the adapter
        adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
