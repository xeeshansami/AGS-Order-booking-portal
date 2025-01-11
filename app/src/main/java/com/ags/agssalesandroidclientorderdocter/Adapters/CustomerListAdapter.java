package com.ags.agssalesandroidclientorderdocter.Adapters;

import com.ags.agssalesandroidclientorderdocter.Models.EntityCustomer;

import com.ags.agssalesandroidclientorderdocter.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asad on 10/1/2016.
 */
public class CustomerListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<EntityCustomer> customerItems;

    public CustomerListAdapter(Activity activity, List<EntityCustomer> customerItems){

        this.activity = activity;
        this.customerItems = customerItems;

    }

    @Override
    public int getCount() {
        return customerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return customerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_customer_row, null);

        TextView customerId = (TextView) convertView.findViewById(R.id.customerId);
        TextView customerName = (TextView) convertView.findViewById(R.id.customerName);
        TextView customerBranch = (TextView) convertView.findViewById(R.id.customerBranch);
        EntityCustomer customer = customerItems.get(position);
        customerId.setText(String.valueOf(customer.getCustomerId()));
        customerName.setText(customer.getCustomerName());
        customerBranch.setText(customer.getCustomerAddress());

        return convertView;
    }
}
