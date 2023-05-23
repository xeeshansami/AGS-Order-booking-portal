package com.ags.agssalesandroidclientorder.Utils;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.ags.agssalesandroidclientorder.R;

public class TransparentProgressDialog extends Dialog {
    public TransparentProgressDialog(Context context) {
        super(context, R.style.Loader);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setTitle(null);
        }
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(R.layout.loader_dialog);
    }
}