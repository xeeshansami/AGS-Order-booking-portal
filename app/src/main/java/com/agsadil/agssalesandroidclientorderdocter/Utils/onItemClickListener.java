package com.agsadil.agssalesandroidclientorderdocter.Utils;

import android.view.View;
import android.widget.ImageView;

import com.agsadil.agssalesandroidclientorderdocter.Models.EntityOrder;

public interface onItemClickListener {
   void onItemClick(View view, int position, EntityOrder order, ImageView imageView);
}


