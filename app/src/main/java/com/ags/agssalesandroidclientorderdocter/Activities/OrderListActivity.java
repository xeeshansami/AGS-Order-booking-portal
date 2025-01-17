package com.ags.agssalesandroidclientorderdocter.Activities;

import com.ags.agssalesandroidclientorderdocter.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorderdocter.Models.EntityOrder;
import com.ags.agssalesandroidclientorderdocter.Utils.SharedPreferenceHandler;
import com.ags.agssalesandroidclientorderdocter.Utils.Utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.ags.agssalesandroidclientorderdocter.R;
import com.ags.agssalesandroidclientorderdocter.Utils.onItemClickListener;
import com.ags.agssalesandroidclientorderdocter.Adapters.OrderListAdapter;

public class OrderListActivity extends AppCompatActivity {

    private DatabaseHandler db;

    private ArrayList<EntityOrder> orderList = new ArrayList<EntityOrder>();
    private RecyclerView recyclerView;
    private OrderListAdapter adapter;
    private EditText txtProductSearch;

    private Button btnAllOrders;
    private Button btnAllPostedOrders;
    private Button btnAllUnPostedOrders;

    SharedPreferenceHandler sp;

    private List<Integer> selectedItems;

    Integer selectedBtn = 1; // 1 means all orders are selected, 2 means only posted orders are shown, 3 means all unposted orders are shown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        try {
            db = new DatabaseHandler(this);
            orderList = db.getAllOrders();
            selectedItems = new ArrayList<Integer>();
            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            // Sets the Toolbar to act as the ActionBar for this Activities window.
            // Make sure the toolbar exists in the activity and is not null
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("All Orders");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            BindButtonCount();

            // BindSearchProductTextBox();
            // BindOrdersList();


            sp = new SharedPreferenceHandler(getApplicationContext());
        } catch (Exception e) {
            Utils.errorBox(this, e.getMessage());
        }
    }

    public void ShowAllOrdersList(View v) {

        selectedBtn = 1;

        orderList = db.getAllOrders();
        BindOrdersList();

        selectedItems = new ArrayList<Integer>();
        UpdateTotalCount();

    }

    public void errorBox(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("SOMETHING WENT WRONG");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.show();
    }

    public void ShowPostedOrdersList(View v) {

        selectedBtn = 2;

        orderList = db.getAllOrders("1");
        BindOrdersList();


        selectedItems = new ArrayList<Integer>();
        UpdateTotalCount();
    }

    public void ShowUnPostedOrdersList(View v) {

        selectedBtn = 3;

        orderList = db.getAllOrders("0");
        BindOrdersList();


        selectedItems = new ArrayList<Integer>();
        UpdateTotalCount();
    }

    public void ViewDetailsOfOrder(View v) {
        if (selectedItems.size() > 0) {
            if (selectedItems.size() > 1) {
                Toast.makeText(this, "Select only 1 item to view details.", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, ActivityOrderProductsDetail.class);
                i.putExtra("OrderId", selectedItems.get(0));
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "0 items selcted", Toast.LENGTH_SHORT).show();
        }
    }

    private void BindButtonCount() {

        btnAllOrders = (Button) findViewById(R.id.btnAllOrders);
        btnAllPostedOrders = (Button) findViewById(R.id.btnAllPostedOrders);
        btnAllUnPostedOrders = (Button) findViewById(R.id.btnAllUnPostedOrders);

        btnAllOrders.setText("All (" + db.getAllOrdersCount() + ")");
        btnAllPostedOrders.setText("Posted (" + db.getAllPostedOrdersCount() + ")");
        btnAllUnPostedOrders.setText("Un Posted (" + db.getAllUnPostedOrdersCount() + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void BindOrdersList() {
        recyclerView = (RecyclerView) findViewById(R.id.lstOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderListAdapter(this, orderList, new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position, EntityOrder order, ImageView imageView) {
                order.setChecked(!order.isChecked());
                EntityOrder entry = orderList.get(position);
                Integer orderId = Integer.parseInt(entry.getOrderId());
                if (order.isChecked()) {
                    imageView.setVisibility(View.VISIBLE);
                    selectedItems.add(orderId);
                } else {
                    selectedItems.remove(orderId);
                    imageView.setVisibility(View.GONE);
                }
                UpdateTotalCount();
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void DeleteSelectedOrders(View v) {

        if (selectedItems.size() == 0) {
            Toast.makeText(getApplicationContext(), "0 Item selected", Toast.LENGTH_SHORT).show();
        } else {
            if (db.deleteOrders(selectedItems)) {
                Toast.makeText(getApplicationContext(), "Orders deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Orders cannot be deleted right now. Try later.", Toast.LENGTH_SHORT).show();
            }
        }

        switch (selectedBtn) {
            case 1: {
                ShowAllOrdersList(null);
                break;
            }
            case 2: {
                ShowPostedOrdersList(null);
                break;
            }
            case 3: {
                ShowUnPostedOrdersList(null);
                break;
            }
            default: {
                break;
            }
        }

        BindButtonCount();
    }

    public void UnPostelectedOrders(View v) {

        if (selectedItems.size() == 0) {
            Toast.makeText(getApplicationContext(), "0 Item selected", Toast.LENGTH_SHORT).show();
        } else {

            //sp.set

            LayoutInflater li = LayoutInflater.from(OrderListActivity.this);
            View promptsView = li.inflate(R.layout.layout_unport_password, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderListActivity.this);
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText password = (EditText) promptsView.findViewById(R.id.unPostPassword);


            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (password.getText().toString().equals(sp.getpassword())) {
                                        if (db.unPostOrders(selectedItems)) {
                                            Toast.makeText(getApplicationContext(), "Orders un posted successfully.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Orders cannot be un posted right now. Try later.", Toast.LENGTH_SHORT).show();
                                        }

                                        switch (selectedBtn) {
                                            case 1: {
                                                ShowAllOrdersList(null);
                                                break;
                                            }
                                            case 2: {
                                                ShowPostedOrdersList(null);
                                                break;
                                            }
                                            case 3: {
                                                ShowUnPostedOrdersList(null);
                                                break;
                                            }
                                            default: {
                                                break;
                                            }
                                        }

                                        BindButtonCount();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Your current password is wrong. please try again.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


        }
    }

    private void UpdateTotalCount() {
        TextView totalSelected = (TextView) findViewById(R.id.totalSelected);
        totalSelected.setText("Total Selected: " + selectedItems.size());
    }
}
