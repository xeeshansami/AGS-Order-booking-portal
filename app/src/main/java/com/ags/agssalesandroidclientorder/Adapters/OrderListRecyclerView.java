//package com.ags.agssalesandroidclientorder.Adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ags.agssalesandroidclientorder.Models.EntityOrder;
//import com.ags.agssalesandroidclientorder.R;
//import com.ags.agssalesandroidclientorder.Utils.onItemClickListener;
//
//import java.util.ArrayList;
//
//
//public class OrderListRecyclerView extends RecyclerView.Adapter<OrderListRecyclerView.MyOrderListHolder>{
//    ArrayList<EntityOrder> orderItems;
//    Context context;
//    onItemClickListener onItemClickListner;
//
//    public OrderListRecyclerView(ArrayList<EntityOrder> arrayList, Context context) {
//        this.orderItems = arrayList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MyOrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View convertView = layoutInflater.inflate(R.layout.layout_order_row, parent, false);
//        TextView orderId = (TextView) convertView.findViewById(R.id.orderId);
//        TextView customerName = (TextView) convertView.findViewById(R.id.Customer);
//        TextView Saleman = (TextView) convertView.findViewById(R.id.Saleman);
//        TextView OrderStatus = (TextView) convertView.findViewById(R.id.OrderStatus);
//        TextView TotalPrice = (TextView) convertView.findViewById(R.id.TotalPrice);
//        TextView CreatedOn = (TextView) convertView.findViewById(R.id.CreatedOn);
//        TextView TotalUniqueProducts = (TextView) convertView.findViewById(R.id.TotalUniqueProducts);
//        EntityOrder order = orderItems.get(position);
////        convertView.setBackgroundColor(!order.isSeleted() ? Color.CYAN : Color.WHITE);
//        orderId.setText(String.valueOf(order.getOrderId()));
//        customerName.setText(order.getOrderCustName());
//        Saleman.setText("Saleman: " + order.getOrderSalName());
//        OrderStatus.setText(String.valueOf(order.getOrderStatus()));
//        TotalPrice.setText(order.getNetTotal() + " Rs");
//        CreatedOn.setText("Created On: " + order.getorderCreatedOn());
//        TotalUniqueProducts.setText(order.getTotalUniqueProducts() + " Unique Products");
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EntityOrder entry = orderItems.get(position);
//                Integer orderId = Integer.parseInt(entry.getOrderId());
////                if (entry.isSeleted()) {
////                    entry.setSeleted(false);
////                    view.setBackgroundColor(Color.CYAN);
//                    selectedItems.add(orderId);
////                } else {
////                    entry.setSeleted(true);
////                    view.setBackgroundColor(Color.WHITE);
//                    selectedItems.remove(orderId);
////                }
////            }
//        });
//        return new MyOrderListHolder(convertView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyOrderListHolder dashboardHolder, int i) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderItems.size();
//    }
//
//
//    public class MyOrderListHolder extends RecyclerView.ViewHolder {
//        TextView orderId,
//                Saleman,
//                OrderStatus,
//                TotalPrice,
//                CreatedOn, customerName,
//                TotalUniqueProducts;
//
//        public MyOrderListHolder(@NonNull View convertView) {
//            super(convertView);
//            orderId = (TextView) convertView.findViewById(R.id.orderId);
//            customerName = (TextView) convertView.findViewById(R.id.Customer);
//            Saleman = (TextView) convertView.findViewById(R.id.Saleman);
//            OrderStatus = (TextView) convertView.findViewById(R.id.OrderStatus);
//            TotalPrice = (TextView) convertView.findViewById(R.id.TotalPrice);
//            CreatedOn = (TextView) convertView.findViewById(R.id.CreatedOn);
//            TotalUniqueProducts = (TextView) convertView.findViewById(R.id.TotalUniqueProducts);
//        }
//    }
//}