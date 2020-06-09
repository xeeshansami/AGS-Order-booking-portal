package com.ags.agssalesandroidclientorder.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.Notifications;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.onItemClickListener;
import com.ags.agssalesandroidclientorder.Utils.onItemClickListenerForNotifications;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<Notifications> arrayList;
    onItemClickListenerForNotifications onItemClickListener;

    public NotificationAdapter(Context context, ArrayList<Notifications> list, onItemClickListenerForNotifications onItemClickListener) {
        this.context = context;
        this.arrayList = list;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_view, viewGroup, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        TextView notification_header, notification_body, notification_date, read_more;

        MultiViewHolder(@NonNull View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.notification_image);
            notification_header = (TextView) convertView.findViewById(R.id.notification_header);
            notification_body = (TextView) convertView.findViewById(R.id.notification_body);
            notification_date = (TextView) convertView.findViewById(R.id.notification_date);
            read_more = (TextView) convertView.findViewById(R.id.read_more);

        }

        void bind(final Notifications notifications) {
            Glide.with(context).load(notifications.getPicURL()).into(imageView);
            notification_header.setText(notifications.getCompanyName());
            notification_body.setText(notifications.getDescription());
            notification_date.setText(notifications.getDurationStart());
            read_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view,getLayoutPosition(),notifications,imageView);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view,getLayoutPosition(),notifications,imageView);
                }
            });
        }
    }
}