package com.ags.agssalesandroidclientorder.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ags.agssalesandroidclientorder.Activities.LoginActivity;
import com.ags.agssalesandroidclientorder.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    setOnitemClickListner listener;
    setOnitemClickListner listener2;
    setOnitemClickListner listener3;
    public TransparentProgressDialog mProgressDialog;

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
}
