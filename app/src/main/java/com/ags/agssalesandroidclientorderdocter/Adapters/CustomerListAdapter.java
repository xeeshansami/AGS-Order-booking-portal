package com.ags.agssalesandroidclientorderdocter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ags.agssalesandroidclientorderdocter.Models.EntityCustomer;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.interfaces.OnItemClickListenerCustomer;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private Activity activity;
    private List<EntityCustomer> customerItems;
    private OnItemClickListenerCustomer listener;
    int customerID=-1;

    // Constructor for the adapter
    public CustomerListAdapter(Activity activity, int customerID,List<EntityCustomer> customerItems, OnItemClickListenerCustomer listener) {
        this.activity = activity;
        this.customerItems = customerItems;
        this.listener = listener;
        this.customerID=customerID;
    }

    // Create ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer_row, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to the view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EntityCustomer customer = customerItems.get(position);

        holder.customerId.setText(String.valueOf(customer.getCustomerId()));
        holder.customerName.setText(customer.getCustomerName());
        holder.customerBranch.setText(customer.getCustomerAddress());
        holder.customerId.setTextColor(activity.getResources().getColor(R.color.grey)); // or the default color
        holder.customerName.setTextColor(activity.getResources().getColor(R.color.grey)); // or the default color
        holder.customerBranch.setTextColor(activity.getResources().getColor(R.color.grey)); // or the default color

        // Change text color if the customer is selected
        if (customer.getSelectedCustomer()==1) {
            holder.customerId.setTextColor(activity.getResources().getColor(R.color.green));
            holder.customerName.setTextColor(activity.getResources().getColor(R.color.green));
            holder.customerBranch.setTextColor(activity.getResources().getColor(R.color.green));
        }

        // Set an item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(customer); // Pass the selected customer to the listener
            }
        });
    }

    // Get the total number of items
    @Override
    public int getItemCount() {
        return customerItems.size();
    }

    // ViewHolder class to hold the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerId, customerName, customerBranch;

        public ViewHolder(View itemView) {
            super(itemView);
            customerId = itemView.findViewById(R.id.customerId);
            customerName = itemView.findViewById(R.id.customerName);
            customerBranch = itemView.findViewById(R.id.customerBranch);
        }
    }
}
