package com.ags.agssalesandroidclientorder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ags.agssalesandroidclientorder.Adapters.ProductListAdapter;
import com.ags.agssalesandroidclientorder.BuildConfig;
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Utils.Constant;
import com.ags.agssalesandroidclientorder.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceManager;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductOfferActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;
    Utils utils;
    ListView product_offer_recycler_view;
    private List<EntityProduct> productsList = new ArrayList<EntityProduct>();
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_offer);
        sp = new SharedPreferenceHandler(this);
        utils = new Utils(this);
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        product_offer_recycler_view = findViewById(R.id.product_offer_recycler_view);
        myToolbar.setSubtitle("Product Offers");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        downloadMasterData();
    }
    public void downloadMasterData() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    BindProductsList();
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.alertBox(ProductOfferActivity.this, "Internet Connections", "Poor connection, please check your internet connection", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            utils.alertBox(this, "Internet Connections", "Network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    view.dismiss();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    finish();
                    view.dismiss();
                }
            });
        }
    }
    private void BindProductsList() {
        utils.showLoader(this);
        AGSStore.getInstance().getProductOffers(sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.indexOf("}]") + 2));
                    //TODO: PRODUCTS
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        EntityProduct product = new EntityProduct();
                        product.setProductId(Integer.parseInt(jObject.get("prod_id").toString()));
                        product.setProductName(jObject.get("prod_name").toString());
                        product.setProductSize(jObject.get("prod_size").toString());
                        product.setProductPrice(Float.parseFloat(jObject.get("prod_tp").toString()));
                        product.setProductCompany(jObject.get("prod_company").toString());
                        product.setProd_Group_Name(jObject.get("Prod_Group_Name").toString());
                        productsList.add(product);
                    }
                    adapter = new ProductListAdapter(ProductOfferActivity.this, productsList);
                    product_offer_recycler_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.hideLoader();
            }

            @Override
            public void Failure(ErrorResponse response) {
                Toast.makeText(ProductOfferActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                utils.hideLoader();
            }
        });

    }
}
