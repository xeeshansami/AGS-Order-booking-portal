package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProductDetails;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EditSingleProduct extends AppCompatActivity implements View.OnClickListener {
    EntityProductDetails details;
    EditText prodName, ProdPrice, ProdSize, ProdQty, ProdDiscount, ProdBonus;
    Button updateBtn;
    Toolbar myToolbar;
    DatabaseHandler db;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_product);
        db = new DatabaseHandler(this);
        utils = new Utils();
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

    public boolean validation() {
        String qty = ProdQty.getText().toString().trim();
        String discount = ProdDiscount.getText().toString().trim();
        String bonus = ProdBonus.getText().toString().trim();

        if (TextUtils.isEmpty(qty)) {
            ProdQty.setFocusable(true);
            ProdQty.setError("Quantity should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Quantity should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(discount)) {
            ProdDiscount.setFocusable(true);
            ProdDiscount.setError("Discount should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Quantity should not be empty", 1000).show();
            return false;
        } else if (TextUtils.isEmpty(bonus)) {
            ProdBonus.setFocusable(true);
            ProdBonus.setError("Bonus should not be empty");
            Snackbar.make(findViewById(android.R.id.content), "Quantity should not be empty", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update_btn) {
            if (validation()) {
                utils.alertBox(this, "Warning", "Do you want to update this order?", "Yes", "No", new setOnitemClickListner() {
                    @Override
                    public void onClick(DialogInterface view, int i) {
                        updateRecord();
                        view.dismiss();
                    }
                });
            }
        }
    }

    public void updateRecord() {
        try {
            String qty = ProdQty.getText().toString().trim();
            String discount = ProdDiscount.getText().toString().trim();
            String bonus = ProdBonus.getText().toString().trim();
            if (db.updateSingleOrder(String.valueOf(details.getProductId()), qty, discount, bonus)) {
                finish();
            }
        } catch (Exception e) {
            utils.alertBox(this, "Alert", e.getMessage(), "Ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }
}
