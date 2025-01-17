package com.ags.agssalesandroidclientorderdocter.Adapters;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;

import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListener2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asad on 10/3/2016.
 */
public class ProductDetailsListAdapter extends BaseAdapter {
    onItemClickListener2 onItemClickListener;
    private Activity activity;
    private LayoutInflater inflater;
    private List<EntityProductDetails> productDetailItems;

    public ProductDetailsListAdapter(Activity activity, List<EntityProductDetails> productDetailItems,  onItemClickListener2 onItemClickListener){
        this.activity = activity;
        this.productDetailItems = productDetailItems;
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public int getCount() {
        return productDetailItems.size();
    }

    @Override
    public Object getItem(int position) {
        return productDetailItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_product_details_row, null);

        TextView Id = (TextView) convertView.findViewById(R.id.Id);
        TextView Name = (TextView) convertView.findViewById(R.id.Name);
        TextView Size = (TextView) convertView.findViewById(R.id.Size);
        TextView Price = (TextView) convertView.findViewById(R.id.Price);
        TextView Qty = (TextView) convertView.findViewById(R.id.Qty);
        TextView Bonus = (TextView) convertView.findViewById(R.id.Bonus);
        TextView Discount = (TextView) convertView.findViewById(R.id.Discount);
        TextView Total = (TextView) convertView.findViewById(R.id.Total);

        final EntityProductDetails product = productDetailItems.get(position);

        Id.setText(String.valueOf(product.getProductId()));
        Name.setText(String.valueOf(product.getProductName()));
        Size.setText(String.valueOf(product.getProductSize()));
        Price.setText(String.valueOf(product.getProductPrice()));
        Qty.setText(String.valueOf(product.getProductQty()));
        Bonus.setText(String.valueOf(product.getProductBonus()));
        Discount.setText(String.valueOf(product.getProductDiscount()));
        Total.setText(String.valueOf(product.getItemValue()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view,position,product);
            }
        });
        return convertView;
    }
}
