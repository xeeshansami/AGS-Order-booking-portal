package com.ags.agssalesandroidclientorder.classes;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;

import com.ags.agssalesandroidclientorder.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asad on 10/3/2016.
 */
public class OrderListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<EntityOrder> orderItems;

    public OrderListAdapter(Activity activity, List<EntityOrder> orderItems){

        this.activity = activity;
        this.orderItems = orderItems;

    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItems.get(position);
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
            convertView = inflater.inflate(R.layout.layout_order_row, null);

        TextView orderId = (TextView) convertView.findViewById(R.id.orderId);
        TextView customerName = (TextView) convertView.findViewById(R.id.Customer);
        TextView Saleman = (TextView) convertView.findViewById(R.id.Saleman);
        TextView OrderStatus = (TextView) convertView.findViewById(R.id.OrderStatus);
        TextView TotalPrice = (TextView) convertView.findViewById(R.id.TotalPrice);
        TextView CreatedOn = (TextView) convertView.findViewById(R.id.CreatedOn);
        TextView TotalUniqueProducts = (TextView) convertView.findViewById(R.id.TotalUniqueProducts);

        EntityOrder order = orderItems.get(position);

        orderId.setText(String.valueOf(order.getOrderId()));
        customerName.setText(order.getOrderCustName());
        Saleman.setText("Saleman: " + order.getOrderSalName());

        OrderStatus.setText(String.valueOf(order.getOrderStatus()));

        TotalPrice.setText(order.getNetTotal() + " Rs");
        CreatedOn.setText("Created On: " + order.getorderCreatedOn());
        TotalUniqueProducts.setText(order.getTotalUniqueProducts() + " Unique Products");


        return convertView;
    }

}
