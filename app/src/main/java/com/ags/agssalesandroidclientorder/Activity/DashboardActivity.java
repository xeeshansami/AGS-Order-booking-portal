package com.ags.agssalesandroidclientorder.Activity;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityOrderAndDetails;

import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;
import com.ags.agssalesandroidclientorder.R;

import android.app.ProgressDialog;

import com.ags.agssalesandroidclientorder.classes.SharedPreferenceHandler;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.ags.agssalesandroidclientorder.utils.Utils;
import com.ags.agssalesandroidclientorder.utils.setOnitemClickListner;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Dash;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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


    public double div(Double x, Double y) {
        Log.i("beforePercentage", "" + (int) (x / y));
        return (x / y) * 100;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        utils = new Utils();
        setDownloadLayout();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        toolbar.setNavigationIcon(android.R.drawable);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        db = new DatabaseHandler(this);
        sp = new SharedPreferenceHandler(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, OrderFormActivity.class);
                startActivity(intent);

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
                //db.clearAll();
                // Session manager
                session = new SessionManager(getApplicationContext());
                // Create login session
                session.setLogin(false);
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

    public void UploadData(View v) {
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
                    progressDialog.dismiss();
                    utils.alertBox(DashboardActivity.this, "Sync Failed", "Do you want to share with pdf?", "Yes", "No", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            exportPdf();
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
            Toast.makeText(DashboardActivity.this, "All data is already uploaded.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        ChangeSyncButtonState();
    }

    /*    public void createPdf(List<EntityOrderAndDetails> allProdsAndDetails) throws FileNotFoundException, DocumentException {

            String dir = Environment.getExternalStorageDirectory() + File.separator + "myLogs";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, "LogHistory3.pdf");


    //        Cursor c1 = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_LOG, null);
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font head1Font = FontFactory.getFont(FontFactory.COURIER_BOLD, 23f);
            Font head2Font = FontFactory.getFont(FontFactory.COURIER, 10f);
            Font head3Font = FontFactory.getFont(FontFactory.COURIER_BOLD, 10f);

            Paragraph p1 = new Paragraph();
            Paragraph p2 = new Paragraph();
            Paragraph p3 = new Paragraph();

            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);

            p1.setFont(head1Font);
            p2.setFont(head2Font);
            p3.setFont(head3Font);

            p1.add("A.G & Sons Sukkur\n");
            p2.add("Branch of A.G & Sons, Blacksmith Street Off Shahi Bazar Sukkur. P# 071-5625355\n");
            p3.add("Loading Report by MUZAFFAR SAFE wise All Customers from: 01/Mar/2020 To: 16/Mar/2020 \n\n");

            document.add(p1);
            document.add(p2);
            document.add(p3);

            PdfPTable table = new PdfPTable(29);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(1);


            PdfPCell cellOne = new PdfPCell(new Phrase("Location1"));
            PdfPCell cellTwo = new PdfPCell(new Phrase("location"));

            cellOne.setBorder(Rectangle.BOX);
            cellTwo.setBorder(Rectangle.BOX);
            cellOne.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)));
            cellTwo.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)));


            table.addCell(cellOne);
            table.addCell(cellTwo);
            table.addCell("orderAddress");
            table.addCell("orderBranch");
            table.addCell("orderBranchSerial");
            table.addCell("orderCreatedOn");
            table.addCell("orderCustAddress");
            table.addCell("orderCustCode");
            table.addCell("orderCustName");
            table.addCell("oderInvoiceNo");
            table.addCell("orderListDate");
            table.addCell("orderListDetailId");
            table.addCell("orderListDetailProdAmount");
            table.addCell("orderListDetailProdBonus");
            table.addCell("orderListDetailProdCode");
            table.addCell("orderListDetialProdDiscount");
            table.addCell("orderListDetailsProdName");
            table.addCell("orderListDetailProdQty");
            table.addCell("orderListDetailProdRate");
            table.addCell("orderListDetailProdSide");
            table.addCell("orderListId");
            table.addCell("orderListId_FK");
            table.addCell("orderSalCode");
            table.addCell("orderSalName");
            table.addCell("orderTotalDiscount");
            table.addCell("orderTotalGross");
            table.addCell("orderTotalNet");
            table.addCell("orderTotalRemarks");
            table.addCell("orderTownId");

            for (EntityOrderAndDetails details : allProdsAndDetails) {
                table.addCell(details.getLocation1());
                table.addCell(details.getLocation());
                table.addCell(details.getOrderAddress());
                table.addCell(details.getOrderBranch());
                table.addCell(details.getOrderBranchSerial());
                table.addCell(details.getOrderCreatedOn());
                table.addCell(details.getOrderCustAddress());
                table.addCell(details.getOrderCustCode());
                table.addCell(details.getOrderCustName());
                table.addCell(details.getOrderInvoiceNo());
                table.addCell(details.getOrderListDate());
                table.addCell(details.getOrderListDetailId());
                table.addCell(details.getOrderListDetailProdAmount());
                table.addCell(details.getOrderListDetailProdBonus());
                table.addCell(details.getOrderListDetailProdCode());
                table.addCell(details.getOrderListDetailProdDiscount());
                table.addCell(details.getOrderListDetailProdName());
                table.addCell(details.getOrderListDetailProdQty());
                table.addCell(details.getOrderListDetailProdRate());
                table.addCell(details.getOrderListDetailProdSize());
                table.addCell(details.getOrderListId());
                table.addCell(details.getOrderListId_FK());
                table.addCell(details.getOrderSalCode());
                table.addCell(details.getOrderSalName());
                table.addCell(details.getOrderTotalDiscount());
                table.addCell(details.getOrderTotalGross());
                table.addCell(details.getOrderTotalNet());
                table.addCell(details.getOrderTotalRemarks());
                table.addCell(details.getOrderTownId());
            }
            document.add(table);
            document.addCreationDate();
            document.close();
        }*/

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

            Font head1Font = FontFactory.getFont(FontFactory.COURIER_BOLD, 23f);
            Font head2Font = FontFactory.getFont(FontFactory.COURIER, 10f);
            Font head3Font = FontFactory.getFont(FontFactory.COURIER_BOLD, 10f);

            Paragraph p1 = new Paragraph();
            Paragraph p2 = new Paragraph();
            Paragraph p3 = new Paragraph();

            p1.setAlignment(Element.ALIGN_CENTER);
            p2.setAlignment(Element.ALIGN_CENTER);
            p3.setAlignment(Element.ALIGN_CENTER);

            p1.setFont(head1Font);
            p2.setFont(head2Font);
            p3.setFont(head3Font);

            p1.add("A.G & Sons Sukkur\n");
            p2.add("Branch of A.G & Sons, Blacksmith Street Off Shahi Bazar Sukkur. Phone# 071-5625355\n");
            p3.add("Loading Report by " + allProdsAndDetails.get(0).getOrderSalName() + " wise All Customers Date: " + allProdsAndDetails.get(0).getOrderListDate() + "\n\n");

            document.add(p1);
            document.add(p2);
            document.add(p3);

            PdfPTable table = new PdfPTable(14);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(1);


            PdfPCell InvoiceNo = new PdfPCell(new Phrase("InvoiceNo"));
            PdfPCell Date = new PdfPCell(new Phrase("Date"));
            PdfPCell Sal_Code = new PdfPCell(new Phrase("Sal_Code"));
            PdfPCell Sal_Name = new PdfPCell(new Phrase("Sal_Name"));
            PdfPCell Cust_Code = new PdfPCell(new Phrase("Cust_Code"));
            PdfPCell Cust_Name = new PdfPCell(new Phrase("Cust_Name"));
            PdfPCell Town_Name = new PdfPCell(new Phrase("Town_Name"));
            PdfPCell Prod_Code = new PdfPCell(new Phrase("Prod_Code"));
            PdfPCell Prod_Name = new PdfPCell(new Phrase("Prod_Name"));
            PdfPCell Size = new PdfPCell(new Phrase("Size"));
            PdfPCell Rate = new PdfPCell(new Phrase("Rate"));
            PdfPCell Qty = new PdfPCell(new Phrase("Qty"));
            PdfPCell Bonus = new PdfPCell(new Phrase("Bonus"));
            PdfPCell Discount = new PdfPCell(new Phrase("Discount"));

            InvoiceNo = headersCell(InvoiceNo);
            Date = headersCell(Date);
            Sal_Code = headersCell(Sal_Code);
            Sal_Name = headersCell(Sal_Name);
            Cust_Code = headersCell(Cust_Code);
            Cust_Name = headersCell(Cust_Name);
            Town_Name = headersCell(Town_Name);
            Prod_Code = headersCell(Prod_Code);
            Prod_Name = headersCell(Prod_Name);
            Size = headersCell(Size);
            Rate = headersCell(Rate);
            Qty = headersCell(Qty);
            Bonus = headersCell(Bonus);
            Discount = headersCell(Discount);


            table.addCell(InvoiceNo);
            table.addCell(Date);
            table.addCell(Sal_Code);
            table.addCell(Sal_Name);
            table.addCell(Cust_Code);
            table.addCell(Cust_Name);
            table.addCell(Town_Name);
            table.addCell(Prod_Code);
            table.addCell(Prod_Name);
            table.addCell(Size);
            table.addCell(Rate);
            table.addCell(Qty);
            table.addCell(Bonus);
            table.addCell(Discount);

            for (EntityOrderAndDetails details : allProdsAndDetails) {
                table.addCell(details.getOrderInvoiceNo());
                table.addCell(details.getOrderListDate());
                table.addCell(details.getOrderSalCode());
                table.addCell(details.getOrderSalName());
                table.addCell(details.getOrderCustCode());
                table.addCell(details.getOrderCustName());
                table.addCell(details.getOrderListDetailProdName());
                table.addCell(details.getOrderListDetailProdBonus());
                table.addCell(details.getOrderTownId());
                table.addCell(details.getOrderListDetailProdCode());
                table.addCell(details.getOrderListDetailProdSize());
                table.addCell(details.getOrderListDetailProdRate());
                table.addCell(details.getOrderListDetailProdQty());
                table.addCell(details.getOrderListDetailProdBonus());
                table.addCell(details.getOrderListDetailProdDiscount());
            }
            document.add(table);
            document.addCreationDate();
            document.close();
        } catch (FileNotFoundException e) {
            utils.alertBox(DashboardActivity.this, "Error", e.getMessage(), "OK", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        } catch (DocumentException e) {
            utils.alertBox(DashboardActivity.this, "Error", e.getMessage(), "OK", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        } finally {
            utils.alertBox(DashboardActivity.this, "Export PDF", "What would you like to do for this file?", "Share", "Cancel", "Open", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            Intent shareIntent = shareFile(file, filename);
                            startActivity(shareIntent);
                            view.dismiss();
                        }
                    }, new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            openFile(file,filename);
                            view.dismiss();
                        }
                    }
            );
        }
    }

    public void ChangeSyncButtonState() {

        TextView txtBtn = (TextView) findViewById(R.id.syncNowBtn);

        if (db.getOrderCount() == 0) {
            txtBtn.setTextColor(Color.BLACK);
            txtBtn.setBackgroundResource(R.color.disableColor);
            txtBtn.setText("Nothing to sync");
        } else {
            txtBtn.setTextColor(Color.WHITE);
            txtBtn.setBackgroundResource(R.color.activeColor);
            txtBtn.setText("Sync Now");
        }

    }


    public PdfPCell headersCell(PdfPCell cell) {
        cell.setBorder(Rectangle.BOX);
        cell.setBackgroundColor(new BaseColor(ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary)));
        return cell;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add:
                startActivity(new Intent(DashboardActivity.this, OrderFormActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.orderPdf:
                exportPdf();
                break;
            case R.id.download:
                if (utils.checkConnection(this)) {
                    if (utils.isNetAvailable(this)) {
                        StartDownloading();
                    } else {
                        utils.alertBox(this, "Internet Connections", "Poor connection", "ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
                    }
                } else {
                    utils.alertBox(this, "Internet Connections", "network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
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
                drawer.closeDrawers();
                break;
            case R.id.orderForm:
                startActivity(new Intent(DashboardActivity.this, OrderFormActivity.class));
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
            default:
                break;
        }
        return true;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public void setDownloadLayout() {
        LayoutInflater li = LayoutInflater.from(DashboardActivity.this);
        promptsView = li.inflate(R.layout.customer_downloader, null);
        alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        mainProgress = promptsView.findViewById(R.id.progress_bar1);
        subProgress = promptsView.findViewById(R.id.progress_bar2);
        category_label = promptsView.findViewById(R.id.category_label);
        cancel_action = promptsView.findViewById(R.id.cancel_action);
        progress_lbl = promptsView.findViewById(R.id.progress_lbl);
        progress_percentage = promptsView.findViewById(R.id.progress_percentage);
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void exportPdf() {
        LayoutInflater li = LayoutInflater.from(DashboardActivity.this);
        promptsView = li.inflate(R.layout.pdf_exporter, null);
        alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        final EditText pdf_name = promptsView.findViewById(R.id.pdf_name);
        location = promptsView.findViewById(R.id.location);
        location.setText("Folder: "+Environment.getExternalStorageDirectory() + File.separator + "AGS");
        Button pdf_name_btn = promptsView.findViewById(R.id.pdf_name_btn);
        pdf_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<EntityOrderAndDetails> allProdsAndDetails = db.getOrderAndDetails(sp.getbranch());
                createPdf(allProdsAndDetails, pdf_name.getText().toString().trim());
                alertDialog.dismiss();
            }
        });
    }

    private Intent shareFile(File file, String filename) {
        Uri uri = FileProvider.getUriForFile(
                this,
                "com.ags.agssalesandroidclientorder.provider", //(use your app signature + ".provider" )
                new File(file.getPath() + "/"+filename + ".pdf"));
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent.createChooser(share, "Share " + filename + ".pdf");
//        share.setPackage("com.whatsapp");
        return share;
    }

    public void openFile(File file,String filename) {
        Uri uri = FileProvider.getUriForFile(
                this,
                "com.ags.agssalesandroidclientorder.provider", //(use your app signature + ".provider" )
                new File(file.getPath() + "/"+filename + ".pdf"));
        try {
            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
            intentUrl.setDataAndType(uri, "application/pdf");
            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentUrl);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DashboardActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
        }
    }

    public void StartDownloading() {
        new LoadUrls().execute();
    }

    public class LoadUrls extends AsyncTask<String, Integer, List<JSONArray>> {
        String customersUrl, productsUrl, salesmanUrl;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            utils.showLoader(DashboardActivity.this);
            customersUrl = "http://mobile.agssukkur.com/agssalesclient.asmx/customers?branch=" + sp.getbranch();
            productsUrl = "http://mobile.agssukkur.com/agssalesclient.asmx/products?branch=" + sp.getbranch();
            salesmanUrl = "http://mobile.agssukkur.com/agssalesclient.asmx/salesman?branch=" + sp.getbranch();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<JSONArray> doInBackground(String... strings) {
            List<JSONArray> jsonArrays = new ArrayList<>();
            jsonArrays.add(customer(customersUrl));
            jsonArrays.add(products(productsUrl));
            jsonArrays.add(salesman(salesmanUrl));
            return jsonArrays;
        }

        @Override
        protected void onPostExecute(List<JSONArray> jsonArrays) {
            super.onPostExecute(jsonArrays);
            jsonArrayForCustomers = jsonArrays.get(0);
            jsonArrayForProducts = jsonArrays.get(1);
            jsonArrayForSalesman = jsonArrays.get(2);
            utils.hideLoader();
            new Downloading().execute();
        }
    }

    public JSONArray customer(String customerUrl) {
        JSONArray jsonArray = null;
        try {
            URL url = new URL(customerUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            String response = new String(builder.toString());
            jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonArray;
        }
    }

    public JSONArray products(String productsUrl) {
        JSONArray jsonArray = null;
        try {
            URL url = new URL(productsUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            String response = new String(builder.toString());
            jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonArray;
        }
    }

    public JSONArray salesman(String salesmanUrl) {
        JSONArray jsonArray = null;
        try {
            URL url = new URL(salesmanUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            String response = new String(builder.toString());
            jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonArray;
        }
    }

    public class Downloading extends AsyncTask<Void, Integer, String> {
        int maxValue = jsonArrayForCustomers.length() + jsonArrayForProducts.length() + jsonArrayForSalesman.length();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
            alertDialog.setCancelable(false);
            subProgress.setMax(100);
            subProgress.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            subProgress.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                final JSONArray customers = jsonArrayForCustomers;
                List<EntityCustomer> allCustomers = new ArrayList<EntityCustomer>();
                for (i = 0; i < customers.length(); i++) {
                    percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(customers.length())));
                    publishProgress((int) percent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            category_label.setText("Importing Customers....");
                            progress_lbl.setText(i + "/" + customers.length());
                        }
                    });
                    JSONObject jObject = customers.getJSONObject(i);
                    EntityCustomer customer = new EntityCustomer();
                    customer.setCustomerId(Integer.parseInt(jObject.get("ACCOUNT_CODE").toString()));
                    customer.setCustomerName(jObject.get("ACCOUNT_NAME").toString());
                    customer.setCustomerBranch(jObject.get("ACCOUNT_TOWN_NAME").toString() + " " + jObject.get("Account_Address").toString());
                    allCustomers.add(customer);
                }
                final JSONArray products = jsonArrayForProducts;
                List<EntityProduct> allProducts = new ArrayList<EntityProduct>();
                for (i = 0; i < products.length(); i++) {
                    percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(maxValue)));
                    publishProgress((int) percent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            category_label.setText("Importing Products....");
                            progress_lbl.setText(i + "/" + products.length());
                        }
                    });
                    JSONObject jObject = products.getJSONObject(i);
                    EntityProduct product = new EntityProduct();
                    product.setProductId(Integer.parseInt(jObject.get("prod_id").toString()));
                    product.setProductName(jObject.get("prod_name").toString());
                    product.setProductSize(jObject.get("prod_size").toString());
                    product.setProductPrice(Float.parseFloat(jObject.get("prod_tp").toString()));
                    product.setProductCompany(jObject.get("prod_company").toString());
                    product.setProd_Group_Name(jObject.get("Prod_Group_Name").toString());
                    allProducts.add(product);
                }
                final JSONArray salesman = jsonArrayForSalesman;
                List<EntitySalesman> allSalesMan = new ArrayList<EntitySalesman>();
                for (i = 0; i < salesman.length(); i++) {
                    percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(salesman.length())));
                    publishProgress((int) percent);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            category_label.setText("Importing Salesmans....");
                            progress_lbl.setText(i + "/" + salesman.length() + 1);
                        }
                    });
                    JSONObject jObject = salesman.getJSONObject(i);
                    EntitySalesman sman = new EntitySalesman();
                    sman.setSalesman_Id(Integer.parseInt(jObject.get("Salesmen_Code").toString()));
                    sman.setSalesman_Name(jObject.get("Salesmen_Name").toString());
                    allSalesMan.add(sman);
                }
                db.addAllCustomers(allCustomers);
                db.addAllProducts(allProducts);
                db.addAllSalesMan(allSalesMan);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            subProgress.setProgress(100);
            category_label.setText("Importing Completed");
            alertDialog.dismiss();
            utils.alertBox(DashboardActivity.this, "", "Master data've download completed", "Done", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }

}
