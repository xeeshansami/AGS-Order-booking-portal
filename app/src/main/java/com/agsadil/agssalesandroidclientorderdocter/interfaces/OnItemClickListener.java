package com.agsadil.agssalesandroidclientorderdocter.interfaces;

import com.agsadil.agssalesandroidclientorderdocter.Models.EntityProduct;

public interface OnItemClickListener {
    void onItemClick(EntityProduct product);
    void onItemLongClick(EntityProduct product);
}