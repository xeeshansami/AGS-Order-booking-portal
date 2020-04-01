package com.ags.agssalesandroidclientorder.utils


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.ags.agssalesandroidclientorder.R


import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
//import com.tuyenmonkey.mkloader.MKLoader

class TransparentProgressDialog(context: Context) : Dialog(context, R.style.Loader) {
    //    private val iv: ImageView
    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
//        val layout = LinearLayout(context)
//        layout.setBackgroundColor(Color.TRANSPARENT)
//        layout.orientation = LinearLayout.VERTICAL
//        val params = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        iv = ImageView(context)
//        params.height = 400
//        params.width = 800
//        params.gravity = Gravity.CENTER
//        iv.setBackgroundColor(Color.TRANSPARENT)
//        val imageViewTarget = DrawableImageViewTarget(iv)
//        Glide.with(context).load(R.raw.loader).into(imageViewTarget)
//        val loader = MKLoader(context)
//        layout.addView(iv, params)
        setContentView(R.layout.loader_dialog)
//        val view =
//        addContentView(R.layout.loader_dialog, params)
    }
}