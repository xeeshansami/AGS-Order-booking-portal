package com.ags.agssalesandroidclientorder.Utils;

import android.view.View;
import android.widget.ImageView;

import com.ags.agssalesandroidclientorder.Models.EntityOrder;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;

public interface onItemClickListener2 {
   void onItemClick(View view, int position, EntityProductDetails order);
}
