package com.ags.agssalesandroidclientorder.Utils;

import android.view.View;
import android.widget.ImageView;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.Notifications;

public interface onItemClickListenerForNotifications {
   void onItemClick(View view, int position, Notifications notifications, ImageView imageView);
}


