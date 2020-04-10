package com.ags.agssalesandroidclientorder.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.onItemClickListener;

import java.util.ArrayList;

public class MultiAdapter extends RecyclerView.Adapter<MultiAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<EntityOrder> arrayList;
    onItemClickListener onItemClickListener;
    public MultiAdapter(Context context, ArrayList<EntityOrder> list,onItemClickListener onItemClickListener) {
        this.context = context;
        this.arrayList = list;
        this.onItemClickListener=onItemClickListener;
    }

    public void setArrayList(ArrayList<EntityOrder> arrayList) {
        this.arrayList = new ArrayList<>();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_row, viewGroup, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(arrayList.get(position), multiViewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        TextView orderId, customerName, Saleman, OrderStatus, TotalPrice, CreatedOn, TotalUniqueProducts;

        MultiViewHolder(@NonNull View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.imageView);
            orderId = (TextView) convertView.findViewById(R.id.orderId);
            customerName = (TextView) convertView.findViewById(R.id.Customer);
            Saleman = (TextView) convertView.findViewById(R.id.Saleman);
            OrderStatus = (TextView) convertView.findViewById(R.id.OrderStatus);
            TotalPrice = (TextView) convertView.findViewById(R.id.TotalPrice);
            CreatedOn = (TextView) convertView.findViewById(R.id.CreatedOn);
            TotalUniqueProducts = (TextView) convertView.findViewById(R.id.TotalUniqueProducts);
        }

        void bind(final EntityOrder order, final View multiViewHolder) {
            imageView.setVisibility(order.isChecked() ? View.VISIBLE : View.GONE);
            orderId.setText(String.valueOf(order.getOrderId()));
            customerName.setText(order.getOrderCustName());
            Saleman.setText(order.getOrderSalName());
            OrderStatus.setText(String.valueOf(order.getOrderStatus()));
            TotalPrice.setText("Rs." + order.getNetTotal());
            CreatedOn.setText(order.getorderCreatedOn());
            TotalUniqueProducts.setText(order.getTotalUniqueProducts() + " Unique Products");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view,getLayoutPosition(),order,imageView);
                }
            });
        }
    }

    public ArrayList<EntityOrder> getAll() {
        return arrayList;
    }

    public ArrayList<EntityOrder> getSelected() {
        ArrayList<EntityOrder> selected = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).isChecked()) {
                selected.add(arrayList.get(i));
            }
        }
        return selected;
    }
}