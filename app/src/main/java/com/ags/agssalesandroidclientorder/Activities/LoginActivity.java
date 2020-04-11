package com.ags.agssalesandroidclientorder.Activities;

import com.ags.agssalesandroidclientorder.Database.DatabaseHandler;
import com.ags.agssalesandroidclientorder.Models.EntityCustomer;
import com.ags.agssalesandroidclientorder.Models.EntityProduct;
import com.ags.agssalesandroidclientorder.Models.EntitySalesman;

import android.Manifest;

import com.ags.agssalesandroidclientorder.BuildConfig;

import android.app.ProgressDialog;

import com.ags.agssalesandroidclientorder.Utils.SessionManager;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ags.agssalesandroidclientorder.Utils.Constant;
import com.ags.agssalesandroidclientorder.Utils.FontImprima;
import com.ags.agssalesandroidclientorder.Utils.OnConnectionCallback;
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceManager;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ags.agssalesandroidclientorder.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    boolean isSalesmansDonwloadOrNot = true, isCustomersDonwloadOrNot = true, isProductsDonwloadOrNot = true;
    JSONArray jsonArrayForCustomers, jsonArrayForProducts, jsonArrayForSalesman;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    View promptsView;
    TextView category_label, progress_lbl, progress_percentage, version_name_lbl;
    Button cancel_action;
    ProgressBar mainProgress;
    ProgressBar subProgress;
    int i = 0;
    double percent = 0.0;
    DatabaseReference databse;
    Utils utils;
    private SessionManager session;
    private boolean loggedIn = false;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
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
    Button btnSignup, btnLogin;
    ImageView hideImage1;
    boolean showHide = true;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferenceHandler(this);
        setContentView(R.layout.activity_login);
        utils = new Utils();
        Log.i("packageNameCheck", " = " + getApplicationContext().getPackageName());
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        hideImage1 = findViewById(R.id.hideshow_img);
        version_name_lbl = findViewById(R.id.version_name_lbl);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnLogin2);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        version_name_lbl.setText("Version: " + BuildConfig.VERSION_NAME);
        databse = FirebaseDatabase.getInstance().getReference("ConsumerAppVersion");
        db = new DatabaseHandler(this);
        myToolbar.setSubtitle("Sign in");
        myToolbar.setNavigationIcon(R.drawable.ic_login);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        myToolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        signUpBtnCheck();
        setUpDownloadAlertBox();
        new FontImprima(this, txtUsername);
        new FontImprima(this, txtPassword);
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
        long different = startDate.getTime() - endDate.getTime();

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
        System.out.printf("dateCheck=> %d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays > 0) {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        } else {
            if (elapsedHours < 24) {
                long remHrs = 23 - elapsedHours;
                long remMins = 60 - elapsedMinutes;
                utils.alertBox(this, "Alert", "Your request for sign up has been already submitted from this device. Please contact Admin for quick approval. Re-submission sign up from this device will be enabled after "+remHrs + " hr(s) & "+remMins +" min(s)",
                        "Ok", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                            }
                        });
            } else {
                SharedPreferenceManager.getInstance(LoginActivity.this).removeStringInSharedPreferences(Constant.signupTime, "remove");
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        }
    }

    public void date(String perviousDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        try {
            String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());
            Date date1 = simpleDateFormat.parse(currentTime);
            Date date2 = simpleDateFormat.parse(perviousDate);
            Log.i("currentTime", " = " + date1);
            printDifference(date1, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void signUpBtnCheck() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime)) &&
                        SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime) != null) {
                    String previousTime = SharedPreferenceManager.getInstance(LoginActivity.this).getStringFromSharedPreferences(Constant.signupTime);
                    Log.i("previousTime", " = " + previousTime);
                    date(previousTime);
                } else {
                    SharedPreferenceManager.getInstance(LoginActivity.this).removeStringInSharedPreferences(Constant.signupTime, "remove");
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                }
            }
        });
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

    public void onResume() {
        super.onResume();
        checkForPermissions();
        if (db != null) {
            db.deleteOrdersOlderThenSevenDays();
        }
    }

    public void isOnlineOffline() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    getAppVersion();
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.hideLoader();
                    utils.myLogs(LoginActivity.this, errorMsg, true);

                }
            }).execute();
        }
    }

    private void checkForPermissions() {
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                isOnlineOffline();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                utils.alertBox(LoginActivity.this, "Permission Denied", "Without permission application will not start, Kinldy accept first all permission",
                        "Again", "Cancel", "Later", new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                                checkForPermissions();
                            }
                        }, new setOnitemClickListner() {
                            @Override
                            public void onClick(DialogInterface view, int i) {
                                view.dismiss();
                                finish();
                            }
                        });
            }
        });

    }

    public void online() {
        DoLogin();
    }

    public void offline() {
        if (sp.getusername() != null && sp.getusername().equals(txtUsername.getText().toString().trim()) && sp.getpassword() != null && sp.getpassword().equals(txtPassword.getText().toString().trim())) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            utils.hideLoader();
            btnLogin.setEnabled(true);
            btnLogin.setClickable(true);
            utils.alertBox(LoginActivity.this, "Alert", "Username or password is invalid please login again and enabled your internet connection", "ok", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    view.dismiss();
                }
            });
        }
    }

    public void Login() {
        if (validation()) {
            if (utils.checkConnection(this)) {
                new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        online();
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        btnLogin.setEnabled(true);
                        btnLogin.setClickable(true);
                        utils.hideLoader();
                        offline();
                    }
                }).execute();
            } else {
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
                utils.hideLoader();
                offline();
            }
        }
    }

    public void downloadMasterData() {
        if (utils.checkConnection(this)) {
            new Utils.CheckNetworkConnection(this, new OnConnectionCallback() {
                @Override
                public void onConnectionSuccess() {
                    StartDownloading("");
                }

                @Override
                public void onConnectionFail(String errorMsg) {
                    utils.hideLoader();
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                    utils.alertBox(LoginActivity.this, "Internet Connections", "Poor connection, check your internet connection is working or not!", "ok", new setOnitemClickListner() {
                        @Override
                        public void onClick(DialogInterface view, int i) {
                            view.dismiss();
                        }
                    });
                }
            }).execute();
        } else {
            utils.hideLoader();
            btnLogin.setEnabled(true);
            btnLogin.setClickable(true);
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
    }

    public void StartDownloading(String role) {
        if (role.equalsIgnoreCase("Saleman")) {
            isSalesmansDonwloadOrNot = false;
        } else if (role.equalsIgnoreCase("Customer")) {
            isCustomersDonwloadOrNot = false;
        } else {
            isProductsDonwloadOrNot = false;
        }
        new LoadUrls().execute();
    }

    public void ChangeView(String role) {
        if (db.getAllCustomers().size()>0 || db.getAllProducts().size()>0 || db.getAllSalesman().size()>0) {
            utils.hideLoader();
            // Create login session
//            session.setLogin(true);
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            StartDownloading(role);
        }
    }

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
                        } else {
                            utils.update(LoginActivity.this);
                        }
                    } catch (Exception e) {
                        utils.hideLoader();
                        utils.myLogs(LoginActivity.this, e.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    utils.hideLoader();
                    utils.myLogs(LoginActivity.this, databaseError.getMessage());
                }

            });
        } catch (Exception e) {
            utils.hideLoader();
            utils.myLogs(LoginActivity.this, e.getMessage());
        }
    }

    // endregion
    // region volley request methods
    public void DoLogin() {
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
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
                                ChangeView(jsonObject.get("role").toString());
                            } else {
                                utils.hideLoader();
                                btnLogin.setEnabled(true);
                                btnLogin.setClickable(true);
                                utils.alertBox(LoginActivity.this, "Alert", "Login Failed, Invalid username or password!", "ok", new setOnitemClickListner() {
                                    @Override
                                    public void onClick(DialogInterface view, int i) {
                                        view.dismiss();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            btnLogin.setEnabled(true);
                            btnLogin.setClickable(true);
                            utils.hideLoader();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
                utils.hideLoader();
                Toast.makeText(LoginActivity.this, "Some error occurred in authentication. Kindly inform administrator.", Toast.LENGTH_SHORT).show();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void setUpDownloadAlertBox() {
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        promptsView = li.inflate(R.layout.customer_downloader, null);
        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        Toolbar toolbar = promptsView.findViewById(R.id.toolbar);
        toolbar.setSubtitle("Master data");
        toolbar.setTitle("Downloading...");
        toolbar.setNavigationIcon(R.drawable.ic_download);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimary));
        mainProgress = promptsView.findViewById(R.id.progress_bar1);
        subProgress = promptsView.findViewById(R.id.progress_bar2);
        category_label = promptsView.findViewById(R.id.category_label);
        progress_lbl = promptsView.findViewById(R.id.progress_lbl);
        cancel_action = promptsView.findViewById(R.id.cancel_action);
        progress_percentage = promptsView.findViewById(R.id.progress_percentage);
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Importing cancelled", 1500).show();
                utils.hideLoader();
                alertDialog.dismiss();
                // Create login session
//                session.setLogin(true);
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
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
        /*    if (isSalesmansDonwloadOrNot) {
                List<JSONArray> jsonArrays = new ArrayList<>();
                jsonArrays.add(customer(customersUrl));
                jsonArrays.add(products(productsUrl));
                return jsonArrays;
            } else if (isCustomersDonwloadOrNot) {
                List<JSONArray> jsonArrays = new ArrayList<>();
                jsonArrays.add(products(productsUrl));
                jsonArrays.add(salesman(salesmanUrl));
                return jsonArrays;
            } else {*/
            List<JSONArray> jsonArrays = new ArrayList<>();
            jsonArrays.add(customer(customersUrl));
            jsonArrays.add(products(productsUrl));
            jsonArrays.add(salesman(salesmanUrl));
            return jsonArrays;
//            }
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
//                session.setLogin(true);
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
            failedDownload(e.getMessage(), true);
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
            failedDownload(e.getMessage(), true);
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
            failedDownload(e.getMessage(), true);
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
                final JSONArray salesman = jsonArrayForSalesman;
                final JSONArray customers = jsonArrayForCustomers;
                final JSONArray products = jsonArrayForProducts;
                db.deleteTable();
                try {
                    //TODO: PRODUCTS
                    for (i = 0; i < products.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(maxValue)));
                        publishProgress((int) percent);
                        runOnUiThread(new Runnable() {
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
                    //TODO: SALESMAN
                    for (i = 0; i < salesman.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(salesman.length())));
                        publishProgress((int) percent);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                category_label.setText("Importing Salesmans....");
                                progress_lbl.setText(i + "/" + salesman.length());
                                progress_percentage.setText(Math.floor(percent) + "%");
                            }
                        });
                        JSONObject jObject = salesman.getJSONObject(i);
                        EntitySalesman sman = new EntitySalesman();
                        sman.setSalesman_Id(Integer.parseInt(jObject.get("Salesmen_Code").toString()));
                        sman.setSalesman_Name(jObject.get("Salesmen_Name").toString());
                        db.addAllSalesMan(sman);
                    }
                    //TODO: CUSTOMERS
                    for (i = 0; i < customers.length(); i++) {
                        percent = div(Double.parseDouble(String.valueOf(i)), Double.parseDouble(String.valueOf(customers.length())));
                        publishProgress((int) percent);
                        runOnUiThread(new Runnable() {
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
                        customer.setCustomerBranch(jObject.get("ACCOUNT_TOWN_NAME").toString() + " " + jObject.get("Account_Address").toString());
                        db.addAllCustomers(customer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    failedDownload(e.getMessage(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                failedDownload(e.getMessage(), true);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            subProgress.setProgress(100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    category_label.setText("Please wait...");
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    category_label.setText("Importing Completed");
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    utils.hideLoader();
                    alertDialog.dismiss();
                    // Create login session
//                    session.setLogin(true);
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);

        }
    }

    public Double div(Double x, Double y) {
        Log.i("beforePercentage", "" + (int) (x / y));
        return (x / y) * 100;
    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        if (v.getId() == R.id.btnLogin) {
            Login();
        }
    }

    public void failedDownload(String errror, boolean isOccured) {
        if (isOccured) {
            utils.alertBox(this, "Download failed", "Something went wrong, please try again to login", "Again", "No", "later", new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    utils.showLoader(LoginActivity.this);
                    db.deleteTable();
                    downloadMasterData();
                }
            }, new setOnitemClickListner() {
                @Override
                public void onClick(DialogInterface view, int i) {
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                    finish();
                }
            });
        }
    }
}

