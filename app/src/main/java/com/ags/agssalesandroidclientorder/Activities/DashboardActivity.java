package com.ags.agssalesandroidclientorder.Activities;

import com.ags.agssalesandroidclientorder.BuildConfig;
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityOrderAndDetails;

import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.ags.agssalesandroidclientorder.R;

import android.app.ProgressDialog;

import com.ags.agssalesandroidclientorder.Utils.SessionManager;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ags.agssalesandroidclientorder.Utils.Constant;
import com.ags.agssalesandroidclientorder.Utils.FontImprima;
import com.ags.agssalesandroidclientorder.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorder.Utils.Utils;
import com.ags.agssalesandroidclientorder.Utils.setOnitemClickListner;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private String url_Base = "http://mobile.agssukkur.com/agssalesclient.asmx/";
    private String url_Login = url_Base + "Login";
    DatabaseHandler db;
    SharedPreferenceHandler sp;
    SessionManager session;
    TextView total_Orders_Count;
    TextView total_Amount;
    Utils utils;
    File file;
    TextView total_Products_Count;
    TextView total_Customer_Count;
    ProgressDialog progressDialog;
    DrawerLayout drawer;
    JSONArray jsonArrayForCustomers, jsonArrayForProducts, jsonArrayForSalesman;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    View promptsView;
    TextView category_label, progress_lbl, progress_percentage, location;
    Button cancel_action;
    ProgressBar mainProgress;
    ProgressBar subProgress;
    int i = 0;
    double percent = 0.0;
    protected Font fontHeader;
    protected Font fontCell;
    protected Font fontSelected;
    protected Font fontCustomer;
    BaseFont bfheader;
    BaseFont bfCell;

    public double div(Double x, Double y) {
        Log.i("beforePercentage", "" + (int) (x / y));
        return (x / y) * 100;
    }

    TextView syncBtn, user_title;
    DatabaseReference databse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        syncBtn = (TextView) findViewById(R.id.syncNowBtn);
        syncBtn.setOnClickListener(this);
        utils = new Utils(this);
        databse = FirebaseDatabase.getInstance().getReference("ConsumerAppVersion");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constant.SYNC_MASTER_DATA));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constant.SYNC_ORDERS_DATA));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constant.SYNC_MASTER_DATA_UPDATE_TEXT_VALUES));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(Constant.SYNC_MASTER_DATA_UPDATE_CANCELLED));
        db = new DatabaseHandler(this);
        sp = new SharedPreferenceHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_title = findViewById(R.id.user_title);
        toolbar.setTitle("Dashboard");
        user_title.setText(sp.getrole() + ": " + sp.getUser_Category());
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        toolbar.setNavigationIcon(android.R.drawable);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        TextView footar_version = navigationView.findViewById(R.id.footar_version);
        TextView userid = hView.findViewById(R.id.header_userid);
        TextView usertitle = hView.findViewById(R.id.header_username);
        footar_version.setText(BuildConfig.VERSION_NAME);
        userid.setText("As Role : " + sp.getrole());
        usertitle.setText("UserID: " + sp.getusername());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.isGPSEnabled(DashboardActivity.this)) {
                    startActivity(new Intent(DashboardActivity.this, OrderFormActivity.class));
                } else {
                    utils.alertBox(DashboardActivity.this, "Alert", "For create an order, Kindly first enable your gps locations ", "Enabled", "Later", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            view.dismiss();
                        }
                    });
                }

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateDashboard();
        ChangeSyncButtonState();
    }


    private void populateDashboard() {
        total_Orders_Count = (TextView) findViewById(R.id.total_Orders_Count);
        total_Amount = (TextView) findViewById(R.id.total_Amount);
        total_Products_Count = (TextView) findViewById(R.id.total_Products_Count);
        total_Customer_Count = (TextView) findViewById(R.id.total_Customer_Count);
        total_Orders_Count.setText(String.valueOf(db.getOrderCount()));
        total_Amount.setText(db.getTotalAmount());
        total_Products_Count.setText(String.valueOf(db.getProductCount()));
        total_Customer_Count.setText(String.valueOf(db.getCustomerCount()));
        new FontImprima(this, total_Orders_Count);
        new FontImprima(this, total_Amount);
        new FontImprima(this, total_Products_Count);
        new FontImprima(this, total_Customer_Count);
        new FontImprima(this, total_Orders_Count);
        new FontImprima(this, total_Amount);
        new FontImprima(this, total_Products_Count);
        new FontImprima(this, total_Customer_Count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.allOrders:
                startActivity(new Intent(DashboardActivity.this, OrderListActivity.class));
                break;
            case R.id.logOut:
                logout();
                break;
            default:
                break;
        }
        return true;
    }

    public void LogOut(MenuItem menuItem) {
        logout();
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirm");
        builder.setMessage("Logout? Make sure that you have uploaded your data.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sp.clearAll();
                db.clearAll();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void LoadAllOrders(MenuItem menuItem) {
        Intent intent = new Intent(DashboardActivity.this, OrderListActivity.class);
        startActivity(intent);
    }

    public void UploadData() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    try {
                        utils.showLoader(DashboardActivity.this);
                        databse.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    String version = dataSnapshot.child("latestverion").getValue().toString();
                                    if (BuildConfig.VERSION_NAME.equals(version)) {
                                        utils.alertBox(DashboardActivity.this, syncBtn, "Alert", "Do you want to Upload All Orders?", "Yes", "No", new setOnitemClickListner() {
                                            @Override
                                            public void onClick(DialogInterface view, int i) {
                                                utils.hideLoader();
                                                checkUserActiveOrNot();
                                                view.dismiss();
                                            }
                                        });
                                    } else {
                                        syncBtn.setEnabled(true);
                                        syncBtn.setClickable(true);
                                        utils.hideLoader();
                                        utils.update(DashboardActivity.this);
                                    }
                                } catch (Exception e) {
                                    syncBtn.setEnabled(true);
                                    syncBtn.setClickable(true);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                syncBtn.setEnabled(true);
                                syncBtn.setClickable(true);
                            }

                        });
                    } catch (Exception e) {
                        syncBtn.setEnabled(true);
                        syncBtn.setClickable(true);
                    }


                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    syncBtn.setEnabled(true);
                    syncBtn.setClickable(true);
                    utils.alertBox(DashboardActivity.this, "Internet Connections", "Poor connection", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            syncBtn.setEnabled(true);
            syncBtn.setClickable(true);
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

    public void checkUserActiveOrNot() {
        if (sp.getusername() != null && sp.getpassword() != null && !TextUtils.isEmpty(sp.getusername()) && !TextUtils.isEmpty(sp.getpassword())) {
            utils.showLoader(this);
            AGSStore.getInstance().getLogin(sp.getusername(), sp.getpassword(), new callback() {
                @Override
                public void Success(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString().substring(response.indexOf("{"), response.indexOf("}") + 1));
                        if (Integer.parseInt(jsonObject.get("userid").toString()) > 0) {
                            utils.hideLoader();
                            uploadProductSyncData();
                        } else {
                            syncBtn.setEnabled(true);
                            syncBtn.setClickable(true);
                            utils.hideLoader();
                            utils.alertBox(DashboardActivity.this, "Alert", "You cannot upload your orders because your account've temporary blocked. Kindly contact your admin", "ok", new setOnitemClickListner() {
                                @Override
                                public void onClick(DialogInterface view, int i) {
                                    view.dismiss();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        syncBtn.setEnabled(true);
                        syncBtn.setClickable(true);
                        utils.hideLoader();
                        e.printStackTrace();
                    }
                }

                @Override
                public void Failure(ErrorResponse response) {
                    syncBtn.setEnabled(true);
                    syncBtn.setClickable(true);
                    utils.hideLoader();
                    Toast.makeText(DashboardActivity.this, "Some error occurred in authentication. Kindly inform your administrator.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            syncBtn.setEnabled(true);
            syncBtn.setClickable(true);
            utils.hideLoader();
            utils.alertBox(DashboardActivity.this, "Alert", "Your account is temporary blocked. Kindly contact your admin", "ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }

    public void uploadProductSyncData() {
        progressDialog.setTitle("Sync in progress");
        progressDialog.setMessage("Please wait while we upload your data ...");
        progressDialog.show();
        progressDialog.setTitle("Sync in progress");
        progressDialog.setMessage("Please wait while we upload your data ...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        List<EntityOrderAndDetails> allProdsAndDetails = db.getOrderAndDetails(sp.getbranch());
        if (allProdsAndDetails.size() > 0) {
            final String jsonProds = new Gson().toJson(allProdsAndDetails);

            StringRequest sr = new StringRequest(Request.Method.POST, "http://mobile.agssukkur.com/agssalesclient.asmx/createOrder", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    syncBtn.setEnabled(true);
                    syncBtn.setClickable(true);
                    try {
                        String responseData = URLDecoder.decode(response, "UTF-8");
                        try {
                            JSONObject jObj = new JSONObject(responseData.substring(response.indexOf("{"), response.indexOf("}") + 1));
                            if (Integer.parseInt(jObj.get("status").toString()) == 1) {
                                db.ChangeStatusToPosted();
                                ChangeSyncButtonState();
                                populateDashboard();
                                progressDialog.dismiss();
                                Toast.makeText(DashboardActivity.this, "Sync Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DashboardActivity.this, "Sync Failed" + response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    } catch (UnsupportedEncodingException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    syncBtn.setEnabled(true);
                    syncBtn.setClickable(true);
                    progressDialog.dismiss();
                    utils.alertBox(DashboardActivity.this, "Sync Failed", "Do you want to share with pdf?", "Yes", "No", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            List<EntityOrderAndDetails> allProdsAndDetails = db.getOrderAndDetails(sp.getbranch());
                            if (allProdsAndDetails.size() > 0 && allProdsAndDetails != null) {
                                exportFile(allProdsAndDetails);
                            } else {
                                utils.alertBox(DashboardActivity.this, "Alert", "There is no data to save for export file", "OK", new setOnitemClickListner() {
                                    @Override
                                    public void onClick(DialogInterface view, int i) {
                                        utils.hideLoader();
                                        view.dismiss();
                                    }
                                });
                            }
                        }
                    });
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data", jsonProds);
                    return params;
                }


            };

            int socketTimeout = 50000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            sr.setRetryPolicy(policy);
            // Add the request to the RequestQueue.
            sr.setShouldCache(false);
            queue.add(sr);
        } else {
            syncBtn.setEnabled(true);
            syncBtn.setClickable(true);
            Toast.makeText(DashboardActivity.this, "All data is already uploaded.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        ChangeSyncButtonState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

    public void createTxtFile(List<EntityOrderAndDetails> allProdsAndDetails, final String filename) {
        try {
            StringBuilder text = new StringBuilder();
            for (EntityOrderAndDetails details : allProdsAndDetails) {
                text.append(
                        details.getOrderSalCode() + ", " +
                        details.getOrderSalName() + ", " +
                        details.getOrderCustCode() + ", " +
                        details.getOrderCustName() + ", " +
                        details.getOrderTownId() + ", " +
                        details.getOrderListDetailProdCode() + ", " +
                        details.getOrderListDetailProdName() + ", " +
                        details.getOrderListDetailProdSize() + ", " +
                        details.getOrderListDetailProdRate() + ", " +
                        details.getOrderListDetailProdQty() + ", " +
                        details.getOrderListDetailProdBonus() + ", " +
                        details.getOrderListDetailProdDiscount() + "\n");
            }
            String dir = Environment.getExternalStorageDirectory() + File.separator + "AGS";
            file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdir();
            }
            File gpxfile = new File(file, filename + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved your text file in AGS folder", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            return;
        } finally {
            utils.alertBox(DashboardActivity.this, "Export Text File", "What would you like to do for this file?", "Share", "Cancel", "Open", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent shareIntent = shareFile(file, filename + ".txt");
                            startActivity(shareIntent);
                            view.dismiss();
                        }
                    }, new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            openFile(file, filename + ".txt");
                            view.dismiss();
                        }
                    }
            );
        }
        utils.hideLoader();
    }

    public void createPdf(List<EntityOrderAndDetails> allProdsAndDetails, final String filename) {
        try {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "AGS";
            file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file = new File(dir, filename + ".pdf");
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font head1Font = FontFactory.getFont(FontFactory.HELVETICA, 23f);
            Font head2Font = FontFactory.getFont(FontFactory.HELVETICA, 10f);
            Font head3Font = FontFactory.getFont(FontFactory.HELVETICA, 10f);

            Paragraph p1 = new Paragraph();
            Paragraph p2 = new Paragraph();
            Paragraph p3 = new Paragraph();
            Paragraph customer = new Paragraph();

            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);
            customer.setAlignment(Element.ALIGN_CENTER);
            p1.setFont(head1Font);
            p2.setFont(head2Font);
            p3.setFont(head3Font);
            Date date = new Date();
            String stringDate = DateFormat.getDateTimeInstance().format(date);
        /*    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
            } catch (BadElementException e) {
                utils.hideLoader();
                e.printStackTrace();
            } catch (IOException e) {
                utils.hideLoader();
                e.printStackTrace();
            }
            img.scaleAbsolute(100f, 100f);
            img.setAlignment(Element.ALIGN_CENTER);*/

//            p1.add("A.G & Sons Sukkur\n");
            p2.add("Branch of A.G & Sons, Blacksmith Street Off Shahi Bazar Sukkur. Phone# 071-5625355\n");
            p3.add("Loading Report by " + allProdsAndDetails.get(0).getOrderSalName() + " wise All Customers Date: " + stringDate + "\n\n");
//            document.add(img);
//            document.add(p1);
//            document.add(p2);
            document.add(p3);
            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(100);
            float[] widths = new float[]{40f, 30f, 50f, 30f, 50f, 40f, 60f, 20f, 20f, 20f, 30f, 30f};
            table.setWidths(widths);
            try {
                bfCell = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
                fontHeader = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.WHITE);
                fontCell = new Font(bfheader, 6, 0, BaseColor.DARK_GRAY);
                fontSelected = new Font(bfheader, 6, 0, BaseColor.BLUE);
                fontCustomer = new Font(bfCell, 7, 0, new BaseColor(76, 104, 162));
            } catch (DocumentException e) {
                utils.hideLoader();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                utils.hideLoader();
            }

            table.addCell(createCellForHeader("InvoiceID", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("SalCode", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("SalName", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("CustCode", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("TownName", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("ProductCode", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("ProductName", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("Size", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("Rate", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("Qty", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("Bonus", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));
            table.addCell(createCellForHeader("Disc%", 0, 1, 1, fontHeader, Element.ALIGN_CENTER));

            for (EntityOrderAndDetails details : allProdsAndDetails) {
                table.addCell("");
                table.addCell(createCell(details.getOrderCustName() + " | " + utils.convertDate(details.getOrderListDate()), 12, 2, 1, fontCustomer, Element.ALIGN_LEFT));
                table.addCell(createCellForInvoice(details.getOrderInvoiceNo(), 1, 2, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderSalCode(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderSalName(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderCustCode(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderTownId(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdCode(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdName(), 1, 1, 1, fontCell, Element.ALIGN_LEFT));
                table.addCell(createCell(details.getOrderListDetailProdSize(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdRate(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdQty(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdBonus(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
                table.addCell(createCell(details.getOrderListDetailProdDiscount(), 1, 1, 1, fontCell, Element.ALIGN_CENTER));
            }
            document.add(table);
            document.addCreationDate();
            document.close();
        } catch (
                FileNotFoundException e) {
            utils.hideLoader();
            utils.alertBox(DashboardActivity.this, "Error", e.getMessage(), "OK", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    utils.hideLoader();
                    view.dismiss();
                }
            });
            return;
        } catch (
                DocumentException e) {
            utils.hideLoader();
            utils.alertBox(DashboardActivity.this, "Error", e.getMessage(), "OK", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    utils.hideLoader();
                    view.dismiss();
                }
            });
            return;
        } catch (Exception e) {
            return;
        } finally {
            Toast.makeText(this, "Saved your pdf file in AGS folder", Toast.LENGTH_LONG).show();
            utils.alertBox(DashboardActivity.this, "Export PDF File", "What would you like to do for this file?", "Share", "Cancel", "Open", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent shareIntent = shareFile(file, filename + ".pdf");
                            startActivity(shareIntent);
                            view.dismiss();
                        }
                    }, new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            openFile(file, filename + ".pdf");
                            view.dismiss();
                        }
                    }
            );
        }
        utils.hideLoader();
    }

    public PdfPCell createCellForHeader(String content, int colspan, int rowspan, int border, Font font, int Align) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorder(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Align);
        cell.setBorderColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.table_left_column_bg)));
        cell.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.table_header_bg)));
        return cell;
    }

    public PdfPCell createCellForInvoice(String content, int colspan, int rowspan, int border, Font font, int Align) throws IOException, DocumentException {
        bfheader = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
        fontHeader = new Font(bfheader, 6, 0, BaseColor.BLUE);
        PdfPCell cell = new PdfPCell(new Phrase(content, fontHeader));
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorder(border);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Align);
        cell.setBorderColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.grey)));
        cell.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.table_left_column_bg)));
        return cell;
    }

    public PdfPCell createCell(String content, int colspan, int rowspan, int border, Font font, int Align) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setBorder(border);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.grey)));
        cell.setHorizontalAlignment(Align);
        cell.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.white)));
        return cell;
    }

    public void ChangeSyncButtonState() {
        if (db.getOrderCount() == 0) {
            syncBtn.setTextColor(Color.BLACK);
            syncBtn.setBackgroundResource(R.color.grey);
            syncBtn.setText("No Pending Orders");
        } else {
            syncBtn.setTextColor(Color.WHITE);
            syncBtn.setBackgroundResource(R.color.activeColor);
            syncBtn.setText("Send Order Now");
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add:
                if (utils.isGPSEnabled(DashboardActivity.this)) {
                    startActivity(new Intent(DashboardActivity.this, OrderFormActivity.class));
                } else {
                    utils.alertBox(DashboardActivity.this, "Alert", "For create an order, \nKindly first enable your gps locations ", "Enabled", "Later", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            view.dismiss();
                        }
                    });
                }
                drawer.closeDrawers();
                break;
            case R.id.orderPdf:
                List<EntityOrderAndDetails> allProdsAndDetails = db.getOrderAndDetails(sp.getbranch());
                if (allProdsAndDetails.size() > 0 && allProdsAndDetails != null) {
                    exportFile(allProdsAndDetails);
                } else {
                    utils.alertBox(DashboardActivity.this, "Alert", "There is no data to save for export file", "OK", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            utils.hideLoader();
                            view.dismiss();
                        }
                    });
                }
                drawer.closeDrawers();
                break;
            case R.id.download:
                downloadMasterData();
                drawer.closeDrawers();
                break;
            case R.id.update:
                startActivity(new Intent(DashboardActivity.this, OrderListActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.logout:
                logout();
                drawer.closeDrawers();
                break;
            case R.id.clearMaster:
                deleteMasterData();
                drawer.closeDrawers();
                break;
            case R.id.clearOrders:
                deleteOrders();
                drawer.closeDrawers();
                break;
            case R.id.updateProfile:
                startActivity(new Intent(this, UpdateProfile.class));
                drawer.closeDrawers();
                break;
            case R.id.sendFeedback:
                startActivity(new Intent(this, FeedbackFormActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.productOffer:
                startActivity(new Intent(this, ProductOfferActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.orderStatistics:
                Snackbar.make(findViewById(android.R.id.content), "Feature come soon", 1000).show();
                drawer.closeDrawers();
                break;
            default:
                break;
        }
        return true;
    }

    public void downloadMasterData() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    try {
                        utils.showLoader(DashboardActivity.this);
                        databse.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    String version = dataSnapshot.child("latestverion").getValue().toString();
                                    if (BuildConfig.VERSION_NAME.equals(version)) {
                                        utils.alertBox(DashboardActivity.this, "Alert", "Do you want to download again?", "Yes", "No", new setOnitemClickListner() {
                                            @Override
                                            public void onClick(DialogInterface view, int i) {
                                                utils.hideLoader();
                                                if (sp.getusername() != null && !TextUtils.isEmpty(sp.getusername())) {
                                                    utils.loginOrActiveCheck(false, true, false, null, sp.getusername(), sp.getpassword());
                                                }
                                                view.dismiss();
                                            }
                                        });
                                    } else {
                                        utils.hideLoader();
                                        utils.update(DashboardActivity.this);
                                    }
                                } catch (Exception e) {
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.alertBox(DashboardActivity.this, "Internet Connections", "Poor connection", "ok", new setOnitemClickListner() {
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

    public void deleteOrders() {
        utils.alertBox(this, "Alert", "Are you sure delete all orders", "Yes", "No", new setOnitemClickListner() {
            @Override
            public void onClick(DialogInterface view, int i) {
                utils.showLoader(DashboardActivity.this);
                if (db.delete(0)) {
                    utils.hideLoader();
                    utils.alertBox(DashboardActivity.this, "Congratulation!", "All orders have been deleted successfully", "Ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent intent = new Intent(Constant.SYNC_ORDERS_DATA);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            view.dismiss();
                        }
                    });
                } else {
                    utils.hideLoader();
                    Snackbar.make(findViewById(android.R.id.content), "Something wend wrong, please try again later", 1000).show();
                }
                view.dismiss();
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Receiver", "Broadcast received: " + action);
            if (action.equals(Constant.SYNC_MASTER_DATA)) {
                total_Products_Count.setText(String.valueOf(0));
                total_Customer_Count.setText(String.valueOf(0));
            } else if (action.equals(Constant.SYNC_ORDERS_DATA)) {
                total_Orders_Count.setText(String.valueOf(0));
                total_Amount.setText(String.valueOf(0.00 + ".Rs"));
                if (db.getOrderCount() == 0) {
                    syncBtn.setTextColor(Color.BLACK);
                    syncBtn.setBackgroundResource(R.color.grey);
                    syncBtn.setText("No Pending Orders");
                } else {
                    syncBtn.setTextColor(Color.WHITE);
                    syncBtn.setBackgroundResource(R.color.activeColor);
                    syncBtn.setText("Send Order Now");
                }
            } else if (action.equals(Constant.SYNC_MASTER_DATA_UPDATE_TEXT_VALUES) || action.equals(Constant.SYNC_MASTER_DATA_UPDATE_CANCELLED)) {
                total_Products_Count.setText(String.valueOf(db.getProductCount()));
                total_Customer_Count.setText(String.valueOf(db.getCustomerCount()));
            }
        }
    };

    public void deleteMasterData() {
        utils.alertBox(this, "Alert", "Are you sure delete all master data", "Yes", "No", new setOnitemClickListner() {
            @Override
            public void onClick(DialogInterface view, int i) {
                utils.showLoader(DashboardActivity.this);
                if (db.delete(1)) {
                    utils.hideLoader();
                    utils.alertBox(DashboardActivity.this, "Congratulation!", "Master data have been deleted successfully", "Ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent intent = new Intent(Constant.SYNC_MASTER_DATA);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            view.dismiss();
                        }
                    });
                } else {
                    utils.hideLoader();
                    Snackbar.make(findViewById(android.R.id.content), "Something wend wrong, please try again later", 1000).show();
                }
                view.dismiss();
            }
        });
    }

    public void exportPdf(final List<EntityOrderAndDetails> allProdsAndDetails) {
        LayoutInflater li = LayoutInflater.from(DashboardActivity.this);
        promptsView = li.inflate(R.layout.pdf_exporter, null);
        alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        final EditText pdf_name = promptsView.findViewById(R.id.pdf_name);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdf_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                pdf_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
        pdf_name.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pdf_name, InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(pdf_name, InputMethodManager.SHOW_IMPLICIT);
        location = promptsView.findViewById(R.id.location);
        location.setText("Folder: " + Environment.getExternalStorageDirectory() + File.separator + "AGS");
        Button pdf_name_btn = promptsView.findViewById(R.id.pdf_name_btn);
        pdf_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showLoader(DashboardActivity.this);
                createPdf(allProdsAndDetails, pdf_name.getText().toString().trim());
                alertDialog.dismiss();
            }
        });
    }

    public void exportTxt(final List<EntityOrderAndDetails> allProdsAndDetails) {
        LayoutInflater li = LayoutInflater.from(DashboardActivity.this);
        promptsView = li.inflate(R.layout.txt_exporter, null);
        alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        final EditText pdf_name = promptsView.findViewById(R.id.pdf_name);
        new Handler().postDelayed(new Runnable() {

            public void run() {
                pdf_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                pdf_name.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
        pdf_name.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pdf_name, InputMethodManager.SHOW_FORCED);
        imm.showSoftInput(pdf_name, InputMethodManager.SHOW_IMPLICIT);
        location = promptsView.findViewById(R.id.location);
        location.setText("Folder: " + Environment.getExternalStorageDirectory() + File.separator + "AGS");
        Button pdf_name_btn = promptsView.findViewById(R.id.pdf_name_btn);
        pdf_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showLoader(DashboardActivity.this);
                createTxtFile(allProdsAndDetails, pdf_name.getText().toString().trim());
                alertDialog.dismiss();
            }
        });
    }

    public void exportFile(final List<EntityOrderAndDetails> allProdsAndDetails) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "", 60000);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        View snackView = inflater.inflate(R.layout.file_export, null);
        Button pdf_btn = snackView.findViewById(R.id.pdf_btn);
        Button txt_btn = snackView.findViewById(R.id.file_btn);
        ImageView imageView = (ImageView) snackView.findViewById(R.id.cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                exportPdf(allProdsAndDetails);
            }
        });
        txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                exportTxt(allProdsAndDetails);
            }
        });
        layout.setPadding(0, 0, 0, 0);
        layout.addView(snackView, 0);
        snackbar.show();
    }

    private Intent shareFile(File file, String filename) {
        Uri uri = FileProvider.getUriForFile(this, "com.ags.agssalesandroidclientorder.provider", new File(file.getPath() + "/" + filename));
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent.createChooser(share, "Share " + filename);
//        share.setPackage("com.whatsapp");
        return share;
    }

    public void openFile(File file, String filename) {
        Uri uri = FileProvider.getUriForFile(this, "com.ags.agssalesandroidclientorder.provider", new File(file.getPath() + "/" + filename));
        try {
            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
            intentUrl.setDataAndType(uri, "application/*");
            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentUrl.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intentUrl);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DashboardActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.syncNowBtn) {
            syncBtn.setEnabled(false);
            syncBtn.setClickable(false);
            UploadData();
        }
    }
}
