package com.ags.agssalesandroidclientorder.Adapters;

import com.ags.agssalesandroidclientorder.Models.EntityProduct;

import com.ags.agssalesandroidclientorder.R;
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
public class ProductListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<EntityProduct> productItems;

    public ProductListAdapter(Activity activity, List<EntityProduct> productItems){

        this.activity = activity;
        this.productItems = productItems;

    }

    @Override
    public int getCount() {
        return productItems.size();
    }

    @Override
    public Object getItem(int position) {
        return productItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_product_row, null);

        TextView productId = (TextView) convertView.findViewById(R.id.productId);
        TextView productName = (TextView) convertView.findViewById(R.id.productName);
        TextView productSize = (TextView) convertView.findViewById(R.id.productSize);
        TextView productPrice = (TextView) convertView.findViewById(R.id.productPrice);
        TextView productCompany = (TextView) convertView.findViewById(R.id.productCompany);

        EntityProduct product = productItems.get(position);

        productId.setText(String.valueOf(product.getProductId()));
        productName.setText(product.getProductName());
        productSize.setText(product.getProductSize());
        productPrice.setText(String.valueOf(product.getProductPrice()));
        productCompany.setText(product.getProd_Group_Name());

        return convertView;
    }
}
