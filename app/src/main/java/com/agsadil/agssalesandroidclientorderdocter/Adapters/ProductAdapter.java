package com.agsadil.agssalesandroidclientorderdocter.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.agsadil.agssalesandroidclientorderdocter.Models.PurchaseHistoryItem;
import com.agsadil.agssalesandroidclientorderdocter.R;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PurchaseHistoryItem> products;
    private List<PurchaseHistoryItem> filteredProducts;

    public ProductAdapter(List<PurchaseHistoryItem> products) {
        this.products = products;
        this.filteredProducts = new ArrayList<>(products); // Initialize filtered list
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowHolder = (RowViewHolder) holder;
            PurchaseHistoryItem product = filteredProducts.get(position);
            rowHolder.productId.setText(String.valueOf(product.getItemCode()));
            rowHolder.productName.setText(product.getItemName());
            rowHolder.productQuantity.setText(String.valueOf(product.getQty()));
            rowHolder.productPrice.setText(product.getRate());
            rowHolder.productCompany.setText(String.valueOf(product.getBonus()));
        }
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }


    public static class RowViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productPrice, productCompany, productId;

        public RowViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productSize);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCompany = itemView.findViewById(R.id.productCompany);
            productId = itemView.findViewById(R.id.productId);
        }
    }

    private OnDataChangedListener dataChangedListener;

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.dataChangedListener = listener;
    }

    public void filter(String query) {
        if (TextUtils.isEmpty(query)) {
            filteredProducts = new ArrayList<>(products);
        } else {
            List<PurchaseHistoryItem> filteredList = new ArrayList<>();
            for (PurchaseHistoryItem product : products) {
                if (product.getItemName().toLowerCase().contains(query.toLowerCase())
                        || String.valueOf(product.getItemCode()).contains(query)
                        || String.valueOf(product.getQty()).contains(query)
                        || String.valueOf(product.getRate()).contains(query)
                        || String.valueOf(product.getBonus()).contains(query)
                ) {
                    filteredList.add(product);
                }
            }
            filteredProducts = filteredList;
        }

        if (dataChangedListener != null) {
            dataChangedListener.onDataChanged(filteredProducts.isEmpty());
        }

        notifyDataSetChanged();
    }

    public interface OnDataChangedListener {
        void onDataChanged(boolean isEmpty);
    }
}
