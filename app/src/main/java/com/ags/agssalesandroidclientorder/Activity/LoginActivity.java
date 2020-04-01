package com.ags.agssalesandroidclientorder.Activity;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;

import android.Manifest;

import com.ags.agssalesandroidclientorder.BuildConfig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import com.ags.agssalesandroidclientorder.classes.SharedPreferenceHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.utils.Constant;
import com.ags.agssalesandroidclientorder.utils.FontImprima;
import com.ags.agssalesandroidclientorder.utils.SharedPreferenceManager;
import com.ags.agssalesandroidclientorder.utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ags.agssalesandroidclientorder.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    JSONArray jsonArrayForCustomers, jsonArrayForProducts, jsonArrayForSalesman;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    View promptsView;
    TextView category_label, progress_lbl, progress_percentage;
    Button cancel_action;
    ProgressBar mainProgress;
    ProgressBar subProgress;
    int i = 0;
    double percent = 0.0;
    int responseCode;
    String responseString = "";
    boolean isCheckUpdate = false;
    DatabaseReference databse;
    Utils utils;
    String version;
    private SessionManager session;
    private boolean loggedIn = false;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private DatabaseHandler db;
    private SharedPreferenceHandler sp;

    private String url_Base = "http://mobile.agssukkur.com/agssalesclient.asmx/";
//    private String url_Base = "http://69.267.237.121/agssalesclient.asmx/";

    private String url_Login = url_Base + "Login";
    private String url_download_customers = url_Base + "customers";
    private String url_download_products = url_Base + "products";
    private String url_download_salesman = url_Base + "salesman";

    EditText txtUsername;
    EditText txtPassword;

    ProgressDialog progressDialog;

    boolean prodsDownload = false, customersDownload = false, salesmanDownload = false;
    Button btnLogin2;
    ImageView hideImage1;
    boolean showHide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        setContentView(R.layout.activity_login);
        setDownloadLayout();
        signUpBtnCheck();

        utils = new Utils();
        databse = FirebaseDatabase.getInstance().getReference("ConsumerAppVersion");
        getAppVersion();
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        db = new DatabaseHandler(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setSubtitle("Sign in");
        myToolbar.setNavigationIcon(R.drawable.ic_login);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        new FontImprima(this, txtUsername);
        new FontImprima(this, txtPassword);
        hideImage1 = findViewById(R.id.hideshow_img);
        hideImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide) {
                    hideImage1.setImageResource(R.drawable.ic_show);
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHide = false;
                } else {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hideImage1.setImageResource(R.drawable.ic_hide);
                    showHide = true;
                }
                new FontImprima(LoginActivity.this, txtPassword);
            }
        });
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays > 0) {
            btnLogin2.setEnabled(true);
            btnLogin2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            if (elapsedHours < 24) {
                btnLogin2.setTextColor(getResources().getColor(R.color.disableColor));
                btnLogin2.setBackground(getResources().getDrawable(R.drawable.dashed_border_disabled));
                btnLogin2.setEnabled(false);
            } else {
                btnLogin2.setEnabled(true);
                btnLogin2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void date(String perviousDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        try {
            String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());
            Date date1 = simpleDateFormat.parse(currentTime);
            Date date2 = simpleDateFormat.parse(perviousDate);
            printDifference(date1, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void signUpBtnCheck() {
        btnLogin2 = findViewById(R.id.btnLogin2);
        if (!TextUtils.isEmpty(SharedPreferenceManager.getInstance(this).getStringFromSharedPreferences(Constant.signupTime)) &&
                SharedPreferenceManager.getInstance(this).getStringFromSharedPreferences(Constant.signupTime) != null) {
            String previousTime = SharedPreferenceManager.getInstance(this).getStringFromSharedPreferences(Constant.signupTime);
            date(previousTime);
        } else {
            btnLogin2.setEnabled(true);
            btnLogin2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean validation() {
        String email = txtUsername.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            txtUsername.setError("Username should not be empty");
            txtUsername.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Username should not be empty", 1000).show();
            return false;
        } /*else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Snackbar.make(findViewById(android.R.id.content), "Please enter valid email address", 1000).show();
            return false;
        }*/ else if (TextUtils.isEmpty(pwd)) {
            txtPassword.setError("Password should not be empty");
            txtPassword.setFocusable(true);
            Snackbar.make(findViewById(android.R.id.content), "Password should not be empty", 1000).show();
            return false;
        } else {
            return true;
        }
    }

    public String[] dateTimeSplitter(String time) {
        String[] dateTime = new String[2];
        dateTime[0] = time.split(" ")[0]; // date
        dateTime[1] = time.split(" ")[1]; // time
        return dateTime;
    }

    public void onResume() {
        super.onResume();
        db.deleteOrdersOlderThenSevenDays();
    }

    private void checkForPermissions() {
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                if (validation()) {
                    DoLogin();
                }
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                finish();
                //finish();// permission denied, block the feature. // will add toast here or custom dialog as per requirement
            }
        });

    }

    public void Login(View v) {

        if (isCheckUpdate) {
            if (validation()) {
                if (sp.getusername() != null && sp.getusername().equals(txtUsername.getText().toString().trim()) && sp.getpassword() != null && sp.getpassword().equals(txtPassword.getText().toString().trim())) {
                    HideDialog();
                    // Create login session
                    session.setLogin(true);
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Makes volley request and get response from server. Set shared preference as well
                    checkForPermissions();
                }
            }
        } else {
            getAppVersion();
        }
    }

    public void StartDownloading() {
        new LoadUrls().execute();
    }

    public void ChangeView() {
        if (prodsDownload == true && customersDownload == true && salesmanDownload == true) {
            utils.hideLoader();
            // Create login session
            session.setLogin(true);
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            StartDownloading();
        }
    }

    // region progress dialog methods

    private void getAppVersion() {
        try {
            databse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String version = dataSnapshot.child("latestverion").getValue().toString();
                       /* Map<String, String> map = (Map) dataSnapshot.getValue();
                        version = map.get("latestverion");*/
                        if (BuildConfig.VERSION_NAME.equals(version)) {
                            isCheckUpdate = true;
                        } else {
                            isCheckUpdate = false;
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(getString(R.string.up_title));
                            builder.setMessage(getString(R.string.up_mes));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getString(R.string.up), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=agnsons.agssalesandroidclient")));
                                }
                            });
                            builder.setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAffinity();
                                }
                            });
                            builder.create().show();
                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }

            });
        } catch (Exception e) {
//            Log.i()
        }
    }


    public void update() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update");
        alertDialog.setMessage("Please update your application from playstore");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void ShowDialog(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void HideDialog() {
        progressDialog.dismiss();
    }

    public void ShowLoginDownload() {
        ShowDialog("Login", "Please wait while we are trying to authenticate you ...");
    }

    public void ShowDownloadDialog() {
        ShowDialog("Download In Progress", "Authentication Successful. Downloading configuration data ...");
    }

    // endregion
    // region volley request methods
    public void DoLogin() {
        utils.showLoader(this);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = url_Login + "?uname=" + txtUsername.getText().toString().trim() + "&pwd=" + txtPassword.getText().toString().trim();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString().substring(response.indexOf("{"), response.indexOf("}") + 1));
                            if (Integer.parseInt(jsonObject.get("userid").toString()) > 0) {
                                sp.setuserid(jsonObject.get("userid").toString());
                                sp.setusername(jsonObject.get("username").toString());
                                sp.setpassword(txtPassword.getText().toString().trim());
                                sp.setcategory(jsonObject.get("category").toString());
                                sp.setrole(jsonObject.get("role").toString());
                                sp.setbranch(jsonObject.get("branch").toString());
                                sp.setUser_Category(jsonObject.get("User_Category").toString());
                                ChangeView();
                            } else {
                                utils.hideLoader();
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            utils.hideLoader();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                utils.hideLoader();
                Log.d("New Error", error.toString());
                Toast.makeText(LoginActivity.this, "Some error occured in authentication. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void setDownloadLayout() {
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        promptsView = li.inflate(R.layout.customer_downloader, null);
        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
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
            }
        });
    }

    public void ShowDialogForDetails(int mainPercentage, int subPercentage, int wholeValue, int subWholeValue) {
        mainProgress.setMax(wholeValue);
        subProgress.setMax(subWholeValue);
        mainProgress.setProgress(mainPercentage);
        subProgress.setProgress(subPercentage);
        alertDialog.show();
        alertDialog.setCancelable(false);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.cancel();
                            }
                        });

    }


    public class LoadUrls extends AsyncTask<String, Integer, List<JSONArray>> {
        String customersUrl, productsUrl, salesmanUrl;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (jsonArrays.size() != 0) {
                jsonArrayForCustomers = jsonArrays.get(0);
                jsonArrayForProducts = jsonArrays.get(1);
                jsonArrayForSalesman = jsonArrays.get(2);
                utils.hideLoader();
                new Downloading().execute();
            } else {
                utils.hideLoader();
                // Create login session
                session.setLogin(true);
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

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
            utils.hideLoader();
            // Create login session
            session.setLogin(true);
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public double div(Double x, Double y) {
        Log.i("beforePercentage", "" + (int) (x / y));
        return (x / y) * 100;
    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        if (v.getId() == R.id.btnLogin2) {

        }
    }
};
