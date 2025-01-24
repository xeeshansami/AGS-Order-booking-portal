package com.ags.agssalesandroidclientorderdocter.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorderdocter.Models.Product;
import com.ags.agssalesandroidclientorderdocter.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ROW = 1;

    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getItemViewType(int position) {
        // Return header for the first position, else row
        return position == 0 ? TYPE_HEADER : TYPE_ROW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_row, parent, false);
            return new RowViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowHolder = (RowViewHolder) holder;
            if(position>0) {
                Product product = products.get(position-1);
                rowHolder.productName.setText(product.getProductName());
                rowHolder.productQuantity.setText(String.valueOf(product.getQuantity()));
                rowHolder.productPrice.setText(String.format("$%.2f", product.getPrice()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return products.size() ; // +1 for header
    }

    // ViewHolder for row items
    public static class RowViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productPrice;

        public RowViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    // ViewHolder for header item
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
