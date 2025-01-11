package com.ags.agssalesandroidclientorderdocter.Utils;

import android.view.View;
import android.widget.ImageView;

import com.ags.agssalesandroidclientorderdocter.Models.EntityOrder;

public interface onItemClickListener {
   void onItemClick(View view, int position, EntityOrder order, ImageView imageView);
}


