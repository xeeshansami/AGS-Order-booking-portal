package com.ags.agssalesandroidclientorderdocter.interfaces;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProduct;

public interface OnItemClickListener {
    void onItemClick(EntityProduct product);
    void onItemLongClick(EntityProduct product);
}