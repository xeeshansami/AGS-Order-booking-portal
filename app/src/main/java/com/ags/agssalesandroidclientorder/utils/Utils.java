package com.ags.agssalesandroidclientorder.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.AlertDialog;

import com.ags.agssalesandroidclientorder.Activity.DashboardActivity;
import com.ags.agssalesandroidclientorder.Activity.LoginActivity;
import com.ags.agssalesandroidclientorder.Activity.SessionManager;

import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    setOnitemClickListner listener;
    setOnitemClickListner listener2;
    setOnitemClickListner listener3;
    public TransparentProgressDialog mProgressDialog;

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
    public static boolean isPingAvailable(String myUrl) {
        boolean isAvailable = false;
        try {
            URL url = new URL(myUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 30 second time out.
            httpURLConnection.setConnectTimeout(3000);
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
}
