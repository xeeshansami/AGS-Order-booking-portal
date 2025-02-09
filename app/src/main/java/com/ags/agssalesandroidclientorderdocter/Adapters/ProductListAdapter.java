package com.ags.agssalesandroidclientorderdocter.Adapters;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.interfaces.OnItemClickListener;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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
    public  class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productId, productName, productSize, productPrice, productCompany;
        LinearLayout bonusLayout;
        TextView bonusRate;

        public ProductViewHolder(View view) {
            super(view);
            productId = view.findViewById(R.id.productId);
            productName = view.findViewById(R.id.productName);
            productSize = view.findViewById(R.id.productSize);
            productPrice = view.findViewById(R.id.productPrice);
            productCompany = view.findViewById(R.id.productCompany);
            bonusLayout = view.findViewById(R.id.bonusLayout);
            bonusRate = view.findViewById(R.id.bonusRate);

            // Set item click listener
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
        // Check if the product is selected, and set the text color accordingly
//        holder.productId.setTextColor(context.getResources().getColor(R.color.grey));
//        holder.productName.setTextColor(context.getResources().getColor(R.color.grey));
//        holder.productSize.setTextColor(context.getResources().getColor(R.color.grey));
//        holder.productCompany.setTextColor(context.getResources().getColor(R.color.grey));
//        holder.productPrice.setTextColor(context.getResources().getColor(R.color.grey));
        /*if (product.isSelectedProduct()==1) {
            Log.i("chckZeeshan",product.isSelectedProduct()+" for product id "+product.getProductId());
            holder.productId.setTextColor(context.getResources().getColor(R.color.green));
            holder.productName.setTextColor(context.getResources().getColor(R.color.green));
            holder.productSize.setTextColor(context.getResources().getColor(R.color.green));
            holder.productCompany.setTextColor(context.getResources().getColor(R.color.green));
            holder.productPrice.setTextColor(context.getResources().getColor(R.color.green));
//            holder.bonusLayout.setVisibility(View.VISIBLE);
//            holder.bonusRate.setText(String.valueOf(product.getProductId()));
        }*/ /*else {
            holder.bonusLayout.setVisibility(View.GONE); // Hide bonusLayout if not selected
        }*/

        // Set the product details
        holder.productId.setText(String.valueOf(product.getProductId()));
        holder.productName.setText(product.getProductName());
        holder.productSize.setText("Size: " + product.getProductSize());
        holder.productPrice.setText(String.valueOf("Price: "+product.getProductPrice()));
        holder.productCompany.setText("Company:"+product.getProductCompany());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    // Define the interface for item click listener

}
