package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.view.View;
import android.widget.ImageView;

import com.agsadil.agssalesandroidclientorderdocter.Models.Notifications;

public interface onItemClickListenerForNotifications {
   void onItemClick(View view, int position, Notifications notifications, ImageView imageView);
}


