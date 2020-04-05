package com.ags.agssalesandroidclientorder.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class myLogs {
    public myLogs(Context context, String message) {
        errorBox(context, message);
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
}
