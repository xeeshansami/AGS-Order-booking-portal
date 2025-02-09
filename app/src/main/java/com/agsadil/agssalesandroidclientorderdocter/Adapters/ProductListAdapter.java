package com.agsadil.agssalesandroidclientorderdocter.Adapters;

import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.R;
import com.agsadil.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private List<EntityProduct> productItems;
    private OnItemClickListener onItemClickListener;
    private Context context;

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
        holder.productId.setText(String.valueOf(product.getProductId()));
        holder.productName.setText(product.getProductName());
        holder.productSize.setText("Size: " + product.getProductSize());
        holder.productPrice.setText(String.valueOf("Price: " + product.getProductPrice()));
        holder.productCompany.setText("Company:" + product.getProd_Group_Name());
        String offerLimit = product.getProd_OfferLimit(); // Example: "1/1/2025 12:00:00 AM"
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date offerDate = sdf.parse(offerLimit); // Convert string to Date
            Date today = new Date(); // Get today's date
            if (offerDate != null && offerDate.after(today)) { // Check if offer is in the future (upcoming)
                holder.productId.setTextColor(context.getResources().getColor(R.color.green));
                holder.productName.setTextColor(context.getResources().getColor(R.color.green));
                holder.productSize.setTextColor(context.getResources().getColor(R.color.green));
                holder.productCompany.setTextColor(context.getResources().getColor(R.color.green));
                holder.productPrice.setTextColor(context.getResources().getColor(R.color.green));
                holder.bonusLayout.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .asGif()  // Explicitly tell Glide to load the image as a GIF
                        .load(R.drawable.bonus)  // Replace with your GIF resource or URL
                        .into(holder.bonusImage);
            } else {
                holder.bonusLayout.setVisibility(View.GONE); // Hide bonusLayout if not selected
            }
        } catch (Exception e) {
            Log.i("CrashOnProducts",e.getMessage());
            holder.bonusLayout.setVisibility(View.GONE); // Hide bonusLayout if not selected
            e.printStackTrace(); // Handle parsing error
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public void updateList(List<EntityProduct> newList) {
        productItems.clear();
        productItems.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }
}
