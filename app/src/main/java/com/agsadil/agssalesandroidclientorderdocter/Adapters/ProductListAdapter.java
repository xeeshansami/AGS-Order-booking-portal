package com.agsadil.agssalesandroidclientorderdocter.Adapters;

import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private List<EntityProduct> productItems;
    private OnItemClickListener onItemClickListener;
    private Context context;
    int filterType = 3;

    // Constructor to pass context as well
    public ProductListAdapter(Context context, List<EntityProduct> productItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.productItems = productItems;
        this.onItemClickListener = onItemClickListener;
    }

    // ViewHolder class to hold the views for the list item
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productId, productName, productSize, productPrice, productCompany;
        LinearLayout bonusLayout;
        ImageView bonusImage;

        public ProductViewHolder(View view) {
            super(view);
            productId = view.findViewById(R.id.productId);
            productName = view.findViewById(R.id.productName);
            productSize = view.findViewById(R.id.productSize);
            productPrice = view.findViewById(R.id.productPrice);
            productCompany = view.findViewById(R.id.productCompany);
            bonusLayout = view.findViewById(R.id.bonusLayout);
            bonusImage = view.findViewById(R.id.bonusImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Pass the clicked product object to the listener
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(productItems.get(getAdapterPosition()));
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemLongClick(productItems.get(getAdapterPosition()));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for the list item using context passed to the adapter
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        EntityProduct product = productItems.get(position);
        // Set product details
        holder.productId.setText(String.valueOf(product.getProductId()));
        holder.productName.setText(product.getProductName());
        holder.productSize.setText("Size: " + product.getProductSize());
        holder.productPrice.setText("Price: " + product.getProductPrice());
        holder.productCompany.setText("Company: " + product.getProd_Group_Name());
        String offerLimit = product.getProd_OfferLimit();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH);
            Date offerDate = inputFormat.parse(offerLimit); // Parse using correct format
            // Handle Date Parsing
            SimpleDateFormat todaysDate = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"); // Adjust format as needed
            String todayDate = todaysDate.format(new Date());
            Date todays = todaysDate.parse(todayDate); // Parse using correct format
            if (offerDate.after(todays)) { // Check if the offer is upcoming
                int greenColor = ContextCompat.getColor(context, R.color.green);
                holder.productId.setTextColor(greenColor);
                holder.productName.setTextColor(greenColor);
                holder.productSize.setTextColor(greenColor);
                holder.productCompany.setTextColor(greenColor);
                holder.productPrice.setTextColor(greenColor);
                holder.bonusLayout.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .asGif()
                        .load(R.drawable.bonus)
                        .into(holder.bonusImage);
            } else {
                int greenColor = ContextCompat.getColor(context, R.color.grey);
                holder.productId.setTextColor(greenColor);
                holder.productName.setTextColor(greenColor);
                holder.productSize.setTextColor(greenColor);
                holder.productCompany.setTextColor(greenColor);
                holder.productPrice.setTextColor(greenColor);
                holder.bonusLayout.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            holder.bonusLayout.setVisibility(View.GONE);
            Log.e("DateParseError", "Failed to parse date: " + offerLimit, e);
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void updateList(ArrayList<EntityProduct> newList) {
        productItems.clear();
        productItems.addAll(newList);
        notifyDataSetChanged();
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void updateList(final ArrayList<EntityProduct> newList, int filterType, final ProgressBar progressBar, final String searchQuery) {
        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            ArrayList<EntityProduct> filteredList = new ArrayList<>();
            this.filterType = filterType;
            for (EntityProduct product : newList) {
                try {
                    if (product.getProductName().toLowerCase().contains(searchQuery.toLowerCase())) {
                        filteredList.add(product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mainHandler.post(() -> {
                progressBar.setVisibility(View.GONE);
                productItems.clear();
                productItems.addAll(filteredList);
                Log.i("checkProductsSize", "Count = " + filteredList.size());
                notifyDataSetChanged();
            });
        });
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }
}
