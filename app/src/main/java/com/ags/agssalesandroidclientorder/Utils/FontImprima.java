package com.ags.agssalesandroidclientorder.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Paxees on 10/13/2018.
 */

public class FontImprima {
    Context context;
    public FontImprima(Context context, EditText editText)
    {
        Typeface exo_2_medium = Typeface.createFromAsset(context.getAssets(), "fonts/amaranth_regular.ttf");
        editText.setTypeface(exo_2_medium);
    }
    public FontImprima(Context context, Button button)
    {
        Typeface exo_2_medium = Typeface.createFromAsset(context.getAssets(), "fonts/amaranth_regular.ttf");
        button.setTypeface(exo_2_medium);
    }
    public FontImprima(Context context, TextView textView)
    {
        Typeface exo_2_medium = Typeface.createFromAsset(context.getAssets(), "fonts/amaranth_regular.ttf");
        textView.setTypeface(exo_2_medium);
    }
}
