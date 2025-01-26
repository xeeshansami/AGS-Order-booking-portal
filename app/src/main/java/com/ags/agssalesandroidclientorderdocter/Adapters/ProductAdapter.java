package com.ags.agssalesandroidclientorderdocter.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.ags.agssalesandroidclientorderdocter.Models.Product;
import com.ags.agssalesandroidclientorderdocter.R;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EntityProduct> products;
    private List<EntityProduct> filteredProducts;

    public ProductAdapter(List<EntityProduct> products) {
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
            EntityProduct product = filteredProducts.get(position);
            rowHolder.productName.setText(product.getProductName());
            rowHolder.productCompany.setText(String.valueOf(product.getProductSize()));
            rowHolder.productId.setText(String.valueOf(product.getProductId()));
            rowHolder.Prod_Group_Name.setText(String.valueOf(product.getProd_Group_Name()));
            rowHolder.productQuantity.setText(String.valueOf(product.getProductSize()));
            rowHolder.productPrice.setText(String.format("$%.2f", product.getProductPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }


    public static class RowViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productPrice, productCompany, productId, Prod_Group_Name;

        public RowViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productSize);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCompany = itemView.findViewById(R.id.productCompany);
            productId = itemView.findViewById(R.id.productId);
            Prod_Group_Name = itemView.findViewById(R.id.productGroup);
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
            List<EntityProduct> filteredList = new ArrayList<>();
            for (EntityProduct product : products) {
                if (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(product.getProductId()).contains(query)) {
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
