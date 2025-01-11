package com.ags.agssalesandroidclientorderdocter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ags.agssalesandroidclientorderdocter.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListener2;

import java.util.ArrayList;

public class SelectOrderListAdapter extends RecyclerView.Adapter<SelectOrderListAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<EntityProductDetails> arrayList;
    onItemClickListener2 onItemClickListener;
    public SelectOrderListAdapter(Context context, ArrayList<EntityProductDetails> list, onItemClickListener2 onItemClickListener) {
        this.context = context;
        this.arrayList = list;
        this.onItemClickListener=onItemClickListener;
    }

    public void setArrayList(ArrayList<EntityProductDetails> arrayList) {
        this.arrayList = new ArrayList<>();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_details_row, viewGroup, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(arrayList.get(position), multiViewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        TextView Id  ,Name, Size ,Price ,Qty, Bonus ,Discount ,Total ;
        MultiViewHolder(@NonNull View convertView) {
            super(convertView);
//            imageView = convertView.findViewById(R.id.imageView);

             Id = (TextView) convertView.findViewById(R.id.Id);
             Name = (TextView) convertView.findViewById(R.id.Name);
             Size = (TextView) convertView.findViewById(R.id.Size);
             Price = (TextView) convertView.findViewById(R.id.Price);
             Qty = (TextView) convertView.findViewById(R.id.Qty);
             Bonus = (TextView) convertView.findViewById(R.id.Bonus);
             Discount = (TextView) convertView.findViewById(R.id.Discount);
             Total = (TextView) convertView.findViewById(R.id.Total);
        }

        void bind(final EntityProductDetails product, final View multiViewHolder) {
//            imageView.setVisibility(product.isChecked() ? View.VISIBLE : View.GONE);
            Id.setText(String.valueOf(product.getProductId()));
            Name.setText(String.valueOf(product.getProductName()));
            Size.setText(String.valueOf(product.getProductSize()));
            Price.setText(String.valueOf(product.getProductPrice()));
            Qty.setText(String.valueOf(product.getProductQty()));
            Bonus.setText(String.valueOf(product.getProductBonus()));
            Discount.setText(String.valueOf(product.getProductDiscount()));
            Total.setText(String.valueOf(product.getItemValue()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view,getLayoutPosition(),product);
                }
            });
        }
    }

    public ArrayList<EntityProductDetails> getAll() {
        return arrayList;
    }

    public ArrayList<EntityProductDetails> getSelected() {
        ArrayList<EntityProductDetails> selected = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).isChecked()) {
                selected.add(arrayList.get(i));
            }
        }
        return selected;
    }
}