package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorder.R;

public class EditSingleProduct extends AppCompatActivity implements View.OnClickListener {
    EntityProductDetails details;
    EditText prodName, ProdPrice, ProdSize, ProdQty, ProdDiscount, ProdBonus;
    Button updateBtn;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_product);
        findViewByID();
        myToolbar.setSubtitle("Update");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_primary_color_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (getIntent().hasExtra("order")) {
            details = new EntityProductDetails();
            details = (EntityProductDetails) getIntent().getExtras().get("order");
            setData(details);
        }
    }

    public void setData(EntityProductDetails details) {
        prodName.setText(details.getProductName());
        ProdPrice.setText(String.valueOf(details.getProductPrice()));
        ProdSize.setText(details.getProductSize());
        ProdQty.setText(String.valueOf(details.getProductQty()));
        ProdDiscount.setText(String.valueOf(details.getProductDiscount()));
        ProdBonus.setText(String.valueOf(details.getProductBonus()));
    }

    public void findViewByID() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        prodName = findViewById(R.id.prod_name);
        ProdPrice = findViewById(R.id.prod_price);
        ProdSize = findViewById(R.id.prod_size);
        ProdQty = findViewById(R.id.prod_qty);
        ProdDiscount = findViewById(R.id.prod_discount);
        ProdBonus = findViewById(R.id.prod_bonus);
        updateBtn = findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_btn) {

        }
    }
}
