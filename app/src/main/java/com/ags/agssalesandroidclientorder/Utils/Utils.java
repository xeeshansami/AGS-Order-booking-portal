package com.ags.agssalesandroidclientorder.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ags.agssalesandroidclientorder.Activities.DashboardActivity;
import com.ags.agssalesandroidclientorder.Activities.LoginActivity;
import com.ags.agssalesandroidclientorder.Activities.SignupActivity;
import com.ags.agssalesandroidclientorder.BuildConfig;
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;
import com.ags.agssalesandroidclientorder.Network.IOnConnectionTimeoutListener;
import com.ags.agssalesandroidclientorder.R;
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse;
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback;
import com.ags.agssalesandroidclientorder.Network.store.AGSStore;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils implements IOnConnectionTimeoutListener {
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    View promptsView;
    TextView category_label, progress_lbl, progress_percentage, version_name_lbl;
    Button cancel_action;
    ProgressBar mainProgress;
    ProgressBar subProgress;
    int i = 0;
    double percent = 0.0;
    Context context;
    boolean isSalesmansDonwloadOrNot = true, isCustomersDonwloadOrNot = true, isProductsDonwloadOrNot = true;
    List<JSONArray> jsonMainArrays;
    setOnitemClickListner listener;
    setOnitemClickListner listener2;
    setOnitemClickListner listener3;
    SharedPreferenceHandler sp;
    DatabaseReference database;
    DatabaseHandler db;
    public TransparentProgressDialog mProgressDialog;

    public Utils(Context context) {
        this.context = context;
        db = new DatabaseHandler(context);
        sp = new SharedPreferenceHandler(context);
        database = FirebaseDatabase.getInstance().getReference("ConsumerAppVersion");
        jsonMainArrays = new ArrayList<>();
    }


    public void myLogs(Context context, String message) {
        errorBox(context, message);
    }

    public static void myLogs(Context context, String message, boolean isPrintLog) {
        Log.i(context.getClass().getSimpleName(), message);
    }

    public static void errorBox(final Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("SOMETHING WENT WRONG");
        alertDialog.setMessage(context.getClass().getSimpleName() + " class error \n" + message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
            }
        });
        alertDialog.show();
    }

    public void alertBox(Context context, String title, String msg, String btn1, setOnitemClickListner OnClickListener) {
        listener = OnClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertBox(Context context, String title, String msg, String btn1, String btn2, setOnitemClickListner OnClickListener) {
        listener = OnClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        builder.setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hideLoader();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertBox(Context context, String title, String msg, String btn1, String btn2, setOnitemClickListner OnClickListener, setOnitemClickListner OnClickListener2) {
        listener = OnClickListener;
        listener2 = OnClickListener2;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        builder.setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener2.onClick(dialog, which);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertBox(Context context, final Button button, String title, String msg, String btn1, String btn2, setOnitemClickListner OnClickListener) {
        listener = OnClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        builder.setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hideLoader();
                button.setClickable(true);
                button.setEnabled(true);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertBox(Context context, final TextView button, String title, String msg, String btn1, String btn2, setOnitemClickListner OnClickListener) {
        listener = OnClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        builder.setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hideLoader();
                button.setClickable(true);
                button.setEnabled(true);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertBox(Context context, String title, String msg, String btn1, String btn2, String btn3, setOnitemClickListner btn1ClickListner, setOnitemClickListner btn2ClickListner) {
        listener = btn1ClickListner;
        listener3 = btn2ClickListner;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        builder.setNegativeButton(btn2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(btn3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener3.onClick(dialog, which);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showLoader(Context context) {
        mProgressDialog = new TransparentProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hideLoader() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onConnectionTimeout() {
        alertBox(context, "Alert", "Connection timeout", "ok", new setOnitemClickListner() {
            @Override
            public void onClick(DialogInterface view, int i) {
                view.dismiss();
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();
            }
        });
    }

    public static class CheckNetworkConnection extends AsyncTask<Void, Void, Boolean> {
        boolean isAvailable = false;
        private OnConnectionCallback onConnectionCallback;
        private Context context;

        public CheckNetworkConnection(Context con, OnConnectionCallback onConnectionCallback) {
            super();
            this.onConnectionCallback = onConnectionCallback;
            this.context = con;
        }

        @Override
        protected Boolean doInBackground(Void... strings) {
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // 30 second time out.
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    isAvailable = true;
                }
            } catch (Exception e) {
                isAvailable = false;
                e.printStackTrace();
            }
            return isAvailable;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                onConnectionCallback.onConnectionSuccess();
            } else {
                onConnectionCallback.onConnectionFail("error");
            }
        }
    }

    public String[] dateTimeSplitter(String time) {
        String[] dateTime = new String[2];
        dateTime[0] = time.split(" ")[0]; // date
        dateTime[1] = time.split(" ")[1]; // time
        return dateTime;
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    public void update(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.up_title));
        builder.setMessage(context.getString(R.string.up_mes));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.up), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getApplicationContext().getApplicationContext().getPackageName())));
            }
        });
        builder.setNegativeButton(context.getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
            }
        });
        builder.create().show();
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public void loginOrActiveCheck(boolean isSalesman, boolean isCustomer, boolean isSPO, final Button button, final String username, final String pwd) {
        Login(button, username, pwd);
    }

    public void StartDownloading(String role, Button button) {
        if (role.equalsIgnoreCase("Customer")) {
            getProducts(button, role);
        } else if (role.equalsIgnoreCase("Saleman")) {
            getProducts(button, role);
        } else if (role.equalsIgnoreCase("SPO")) {
            getProductsForSPO(button, role);
        }
    }

    public void getAppVersion() {
        try {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String version = dataSnapshot.child("latestverion").getValue().toString();
                       /* Map<String, String> map = (Map) dataSnapshot.getValue();
                        version = map.get("latestverion");*/
                        if (BuildConfig.VERSION_NAME.equals(version)) {
                        } else {
                            update(context);
                        }
                    } catch (Exception e) {
                        hideLoader();
                        myLogs(context, e.getMessage(), true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideLoader();
                    myLogs(context, databaseError.getMessage(), true);
                }

            });
        } catch (Exception e) {
            hideLoader();
            myLogs(context, e.getMessage(), true);
        }
    }

    public void Login(final Button button, final String username, final String password) {
        AGSStore.getInstance().getLogin(username, password, new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString().substring(response.indexOf("{"), response.indexOf("}") + 1));
                    if (Integer.parseInt(jsonObject.get("userid").toString()) > 0) {
                        sp.setuserid(jsonObject.get("userid").toString());
                        sp.setusername(jsonObject.get("username").toString());
                        sp.setpassword(password);
                        sp.setcategory(jsonObject.get("category").toString());
                        sp.setrole(jsonObject.get("role").toString());
                        sp.setbranch(jsonObject.get("branch").toString());
                        sp.setUser_Category(jsonObject.get("User_Category").toString());
                        if (jsonObject.get("User_comp").toString() != null) {
                            sp.setCompID(jsonObject.get("User_comp").toString());
                        }
                        ChangeView(jsonObject.get("role").toString(), button, username, password);
                    } else {
                        hideLoader();
                        if (((Activity) context).getClass().getSimpleName().equalsIgnoreCase("LoginActivity")) {
                            button.setEnabled(true);
                            button.setClickable(true);
                        }
                        if (sp.getusername() != null) {
                            alertBox(context, "Alert", "Logged in user: " + sp.getusername() + " or password is wrong, or may be your account is blocked, kindly inform your administrator", "ok", new setOnitemClickListner() {
                                @Override
                                public void onClick(DialogInterface view, int i) {
                                    view.dismiss();
                                }
                            });
                        } else {
                            alertBox(context, "Alert", "Username or password is wrong, or may be your account is blocked, kindly inform your administrator", "ok", new setOnitemClickListner() {
                                @Override
                                public void onClick(DialogInterface view, int i) {
                                    view.dismiss();
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    if (((Activity) context).getClass().getSimpleName().equalsIgnoreCase("LoginActivity")) {
                        button.setEnabled(true);
                        button.setClickable(true);
                    }
                    hideLoader();
                    e.printStackTrace();
                }

            }

            @Override
            public void Failure(ErrorResponse response) {
                if (((Activity) context).getClass().getSimpleName().equalsIgnoreCase("LoginActivity")) {
                    button.setEnabled(true);
                    button.setClickable(true);
                }
                hideLoader();
                Toast.makeText(context, "Some error occurred in authentication. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProductsForSPO(final Button button, final String role) {
        AGSStore.getInstance().getProductsForSPO(sp.getCompID(), sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                    jsonMainArrays.add(jsonArray);
                    getCustomer(button, role);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                hideLoader();
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }

    public void getProducts(final Button button, final String role) {
        AGSStore.getInstance().getProducts(sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                    jsonMainArrays.add(jsonArray);
                    if (role.equalsIgnoreCase("Customer")) {
                        getCustomerSelfData(button, role);
                    } else {
                        getSalesmanSelfData(button, role);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }


    public void getCustomerSelfData(final Button button, final String role) {
        AGSStore.getInstance().getSelfCustomer(sp.getbranch(), sp.getcategory(), new callback() {
            @Override
            public void Success(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonMainArrays.add(jsonArray);
                getSalesmanForCustomer(button, role);
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                hideLoader();
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }

    public void getSalesmanForCustomer(final Button button, final String role) {
        AGSStore.getInstance().getSalesmanForCustomer(sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                    jsonMainArrays.add(jsonArray);
                    if (jsonMainArrays.size() != 0) {
                        hideLoader();
                        if (role.equalsIgnoreCase("Customer")) {
                            new Downloading(button, 0).execute();
                        } else {
                            new Downloading(button, 2).execute();
                        }
                    } else {
                        hideLoader();
                        Intent intent = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                hideLoader();
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }

    public void getSalesmanSelfData(final Button button, final String role) {
        AGSStore.getInstance().getSelfSalesman(sp.getcategory(), sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonMainArrays.add(jsonArray);
                getCustomer(button, role);
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                hideLoader();
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }

    public void getCustomer(final Button button, final String role) {
        AGSStore.getInstance().getCustomer(sp.getbranch(), new callback() {
            @Override
            public void Success(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString().substring(response.indexOf("["), response.indexOf("}]") + 2));
                    jsonMainArrays.add(jsonArray);
                    if (role.equalsIgnoreCase("Saleman")) {
                        if (jsonMainArrays.size() != 0) {
                            hideLoader();
                            new Downloading(button, 1).execute();
                        } else {
                            hideLoader();
                            Intent intent = new Intent(context, DashboardActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    } else {
                        getSalesmanForCustomer(button, role);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(ErrorResponse response) {
                button.setEnabled(true);
                button.setClickable(true);
                hideLoader();
                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                hideLoader();
            }
        });
    }


    public void setUpDownloadAlertBox(final Button button) {
        LayoutInflater li = LayoutInflater.from(context);
        promptsView = li.inflate(R.layout.customer_downloader, null);
        alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        Toolbar toolbar = promptsView.findViewById(R.id.toolbar);
        toolbar.setSubtitle("Master data");
        toolbar.setTitle("Downloading...");
        toolbar.setNavigationIcon(R.drawable.ic_download);
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.colorPrimary));
        toolbar.setSubtitleTextColor(context.getResources().getColor(R.color.colorPrimary));
        mainProgress = promptsView.findViewById(R.id.progress_bar1);
        subProgress = promptsView.findViewById(R.id.progress_bar2);
        category_label = promptsView.findViewById(R.id.category_label);
        progress_lbl = promptsView.findViewById(R.id.progress_lbl);
        cancel_action = promptsView.findViewById(R.id.cancel_action);
        progress_percentage = promptsView.findViewById(R.id.progress_percentage);
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                SharedPreferenceManager.getInstance(context).removeStringInSharedPreferences(Constant.AUTO_DOWNLOAD_IN_Day, "remove");
                new Downloading(button, 0).cancel(true);
                int autoDownload = SharedPreferenceManager.getInstance(context).getIntFromSharedPreferences(Constant.AUTO_DOWNLOAD_IN_Day_TXT);
                if (autoDownload != 1) {
                    if (((Activity) context).getClass().getSimpleName().equalsIgnoreCase("LoginActivity")) {
                        button.setEnabled(true);
                        button.setClickable(true);
                        context.startActivity(new Intent(context, DashboardActivity.class));
                        ((Activity) context).finish();
                    } else {
                        Intent intent = new Intent(Constant.SYNC_MASTER_DATA_UPDATE_CANCELLED);
                        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
                    }
                }
                Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Importing cancelled", 1500).show();
                hideLoader();
            }
        });
    }

    public class Downloading extends AsyncTask<Void, Integer, String> {
        Button button;
        int inWhich = 0;
        JSONArray products, customers, salesman;

        public Downloading(Button button, int inWhich) {
            this.button = button;
            this.inWhich = inWhich;
            setUpDownloadAlertBox(button);
        }

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
                db.deleteTable();
                if (inWhich == 2) {
                    products = jsonMainArrays.get(0);
                    customers = jsonMainArrays.get(1);
                    salesman = jsonMainArrays.get(2);


                    //TODO: Salesman
                    for (i = 0; i < salesman.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(salesman.length())));
                        publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Salesman....");
                                progress_lbl.setText(i + "/" + salesman.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
                            }
                        });
                        JSONObject jObjectsalesman = salesman.getJSONObject(0);
                        EntitySalesman sman = new EntitySalesman();
                        sman.setSalesman_Id(Integer.parseInt(jObjectsalesman.get("Salesmen_Code").toString()));
                        sman.setSalesman_Name(jObjectsalesman.get("Salesmen_Name").toString());
                        db.addAllSalesMan(sman);
                    }


                    //TODO: CUSTOMERS
                    for (i = 0; i < customers.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(customers.length())));
                        publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Customers....");
                                progress_lbl.setText(i + "/" + customers.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
                            }
                        });
                        JSONObject jObject = customers.getJSONObject(i);
                        EntityCustomer customer = new EntityCustomer();
                        customer.setCustomerId(Integer.parseInt(jObject.get("ACCOUNT_CODE").toString()));
                        customer.setCustomerName(jObject.get("ACCOUNT_NAME").toString());
                        customer.setCustomerAddress(jObject.get("ACCOUNT_TOWN_NAME").toString() + " " + jObject.get("Account_Address").toString());
                        customer.setCustomerBranch(jObject.get("Account_Branch").toString());
                        db.addAllCustomers(customer);
                    }
                    //TODO: PRODUCTS
                    for (i = 0; i < products.length(); i++) {
                            percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(products.length())));
                            publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Products....");
                                progress_lbl.setText(i + "/" + products.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
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
                        db.addAllProducts(product);
                    }

                } else {
                    if (inWhich == 0) {
                        products = jsonMainArrays.get(0);
                        customers = jsonMainArrays.get(1);
                        salesman = jsonMainArrays.get(2);
                    } else if (inWhich == 1) {
                        products = jsonMainArrays.get(0);
                        salesman = jsonMainArrays.get(1);
                        customers = jsonMainArrays.get(2);
                    }

                    //TODO: Salesman
                    for (i = 0; i < salesman.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(salesman.length())));
                        publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Salesman....");
                                progress_lbl.setText(i + "/" + salesman.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
                            }
                        });
                        JSONObject jObjectsalesman = salesman.getJSONObject(0);
                        EntitySalesman sman = new EntitySalesman();
                        sman.setSalesman_Id(Integer.parseInt(jObjectsalesman.get("Salesmen_Code").toString()));
                        sman.setSalesman_Name(jObjectsalesman.get("Salesmen_Name").toString());
                        db.addAllSalesMan(sman);
                    }

                    //TODO: CUSTOMERS
                    for (i = 0; i < customers.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(customers.length())));
                        publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Customers....");
                                progress_lbl.setText(i + "/" + customers.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
                            }
                        });
                        JSONObject jObject = customers.getJSONObject(i);
                        EntityCustomer customer = new EntityCustomer();
                        customer.setCustomerId(Integer.parseInt(jObject.get("ACCOUNT_CODE").toString()));
                        customer.setCustomerName(jObject.get("ACCOUNT_NAME").toString());
                        customer.setCustomerAddress(jObject.get("ACCOUNT_TOWN_NAME").toString() + " " + jObject.get("Account_Address").toString());
                        customer.setCustomerBranch(jObject.get("Account_Branch").toString());
                        db.addAllCustomers(customer);
                    }

                    //TODO: PRODUCTS
                    for (i = 0; i < products.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(products.length())));
                        publishProgress((int) percent);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Products....");
                                progress_lbl.setText(i + "/" + products.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
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
                        db.addAllProducts(product);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                failedDownload(e.getMessage(), true, button);
            }finally {
                return null;
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            subProgress.setProgress(100);
            // Format time

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    category_label.setText("Please wait...");
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    category_label.setText("Import've Completed");
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (checkActivity(context, "LoginActivity")) {
                        hideLoader();
                        alertDialog.dismiss();
                        button.setEnabled(true);
                        button.setClickable(true);
                        db.delete(2);
                        DateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                        String currentTime = df.format(Calendar.getInstance().getTime());
                        SharedPreferenceManager.getInstance(context).storeStringInSharedPreferences(Constant.AUTO_DOWNLOAD_IN_TIME, currentTime);
                        db.addUserInfo(Integer.parseInt(sp.getuserid()), sp.getusername(), sp.getrole());
                        int autoDownload = SharedPreferenceManager.getInstance(context).getIntFromSharedPreferences(Constant.AUTO_DOWNLOAD_IN_Day_TXT);
                        if (autoDownload != 1) {
                            Intent intent = new Intent(context, DashboardActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    } else {
                        hideLoader();
                        alertDialog.dismiss();
                        // Create login session
                        alertDialog.dismiss();
                        Intent intent = new Intent(Constant.SYNC_MASTER_DATA_UPDATE_TEXT_VALUES);
                        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
                        DateFormat df = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                        String currentTime = df.format(Calendar.getInstance().getTime());
                        SharedPreferenceManager.getInstance(context).storeStringInSharedPreferences(Constant.AUTO_DOWNLOAD_IN_TIME, currentTime);
                        alertBox(context, "", "Master data've download completed", "Done", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
                    }
                }
            }, 2000);
        }

    }

    public Double div(Double x, Double y) {
        Log.i("beforePercentage", "" + (int) (x / y));
        return (x / y) * 100;
    }

    public void failedDownload(String error, boolean isOccurred, final Button button) {
        if (isOccurred) {
            alertBox(context, "Download failed", "Something went wrong, please try again to login", "Again", "No", "Later", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    db.deleteTable();
                    downloadMasterData(button);
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    button.setEnabled(true);
                    button.setClickable(true);
                    ((Activity) context).finish();
                }
            });
        }
    }

    public void downloadMasterData(final Button button) {
        if (checkConnection(context)) {
            showLoader(context);
            new Utils.CheckNetworkConnection(context, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    StartDownloading(sp.getrole(), button);
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    hideLoader();
                    button.setEnabled(true);
                    button.setClickable(true);
                    alertBox(context, "Internet Connections", "Poor connection, check your internet connection is working or not!", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            hideLoader();
            button.setEnabled(true);
            button.setClickable(true);
            alertBox(context, "Internet Connections", "Network not available please check", "Setting", "Cancel", "Exit", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    view.dismiss();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    ((Activity) context).finish();
                    view.dismiss();
                }
            });
        }
    }


    public void ChangeView(String role, final Button button, String username, String password) {
        if (checkActivity(context, "LoginActivity")) {
            int autoDownload = SharedPreferenceManager.getInstance(context).getIntFromSharedPreferences(Constant.AUTO_DOWNLOAD_IN_Day_TXT);
            if (autoDownload == 1) {
                StartDownloading(role, button);
            } else {
                if (db.getAllCustomers().size() > 0 && db.getAllProducts().size() > 0 && db.getAllSalesman().size() > 0) {
                    /*Downloading when user same and have data in local database*/
                    hideLoader();
                    context.startActivity(new Intent(context, DashboardActivity.class));
                    ((Activity) context).finish();
                } else {
                    /*Downloading when user same but there is not downloading in local database*/
                    StartDownloading(role, button);
                }
            }
        } else {
            /*Downloading from dashboard*/
            downloadMasterData(button);
        }
    }

    public static boolean checkActivity(Context context, String activityName) {
        if (((Activity) context).getClass().getSimpleName().equalsIgnoreCase(activityName)) {
            return true;
        } else {
            return false;
        }
    }

    public String convertDate(String dateString) throws Exception {
        String dateInputPattern = "yyyy-MM-dd";
        String dateTargetPattern = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateInputPattern);
        Date date = sdf.parse(dateString);
        sdf.applyPattern(dateTargetPattern);
        System.out.println("Target Pattern: " + dateTargetPattern);
        System.out.println("Converted Date " + sdf.format(date));
        return sdf.format(date);
    }

    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    public String getURLForResource2(int resourceId) {
        Uri path = Uri.parse("android.resource:// com.ags.agssalesandroidclientorder/" + R.drawable.icon);
        String path2 = path.toString();
        return path2;
    }

    public int timeSplitter(String time) {
        String[] items = time.split(":");
        return Integer.parseInt(items[0]);
    }
    private static char[] smallCaps = new char[]
            {
                    '\uf761', //A
                    '\uf762',
                    '\uf763',
                    '\uf764',
                    '\uf765',
                    '\uf766',
                    '\uf767',
                    '\uf768',
                    '\uf769',
                    '\uf76A',
                    '\uf76B',
                    '\uf76C',
                    '\uf76D',
                    '\uf76E',
                    '\uf76F',
                    '\uf770',
                    '\uf771',
                    '\uf772',
                    '\uf773',
                    '\uf774',
                    '\uf775',
                    '\uf776',
                    '\uf777',
                    '\uf778',
                    '\uf779',
                    '\uf77A'   //Z
            };

    public static String getSmallCapsString (String input) {
        char[] chars = input.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = smallCaps[chars[i] - 'a'];
            }
        }
        return String.valueOf(chars);
    }
}
