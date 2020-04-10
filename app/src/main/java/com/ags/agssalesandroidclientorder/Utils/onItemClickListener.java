package com.ags.agssalesandroidclientorder.Utils;

import android.view.View;
import android.widget.ImageView;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;

public interface onItemClickListener {
   void onItemClick(View view, int position, EntityOrder order, ImageView imageView);
}
