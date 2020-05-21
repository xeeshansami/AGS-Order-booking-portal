package com.ags.agssalesandroidclientorder.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback
import com.ags.agssalesandroidclientorder.Network.store.AGSStore
import com.ags.agssalesandroidclientorder.R
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler
import com.ags.agssalesandroidclientorder.Utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.json.JSONObject


@Suppress("DEPRECATION")
class UpdateProfile : AppCompatActivity(), View.OnClickListener {
    var agsStore: AGSStore = AGSStore.getInstance()
    var utils: Utils? = null
    var sp: SharedPreferenceHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        toolbar.subtitle = "Update Profile"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorPrimary))
        toolbar.setSubtitleTextColor(resources.getColor(R.color.colorPrimary))
        toolbar.setNavigationOnClickListener { finish() }
        sp = SharedPreferenceHandler(this)
        utils = Utils(this)
        sp!!.getusername().toString()?.let { login_id.setText(it) }
        sp!!.user_Category.toString()?.let { user_name.setText(it) }
        if (sp?.getemail() != null) {
            sp?.getemail().toString()?.let {
                user_email.setText(it)
            }
        } else {
            user_email.setText("")
        }
        if (sp?.getcontact() != null) {
            sp?.getcontact().toString()?.let {
                user_contact.setText(it)
            }
        } else {
            user_contact.setText("")
        }
        update_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                postUpdateProfile(user_name.text.toString().trim(), user_email.text.toString().trim(), user_contact.text.toString().trim(), sp!!.getuserid())
            }
        })
        val intent = Intent(this, ChangePassword::class.java)
        update_pwd_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(intent)
            }
        })
    }


    fun validation(): Boolean {
        val fullname: String = user_name.getText().toString().trim({ it <= ' ' })
        val email: String = user_email.getText().toString().trim({ it <= ' ' })
        val contact: String = user_contact.getText().toString().trim({ it <= ' ' })
        return if (TextUtils.isEmpty(fullname)) {
            user_name.setFocusable(true)
            user_name.setError("Full name should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Full name should not be empty", 1000).show()
            false
        } else if (TextUtils.isEmpty(email)) {
            user_email.setFocusable(true)
            user_email.setError("Email should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Email should not be empty", 1000).show()
            false
        } else if (!isValidEmail(email)) {
            user_email.setFocusable(true)
            user_email.setError("Please enter valid email address")
            Snackbar.make(findViewById(android.R.id.content), "Please enter valid email address", 1000).show()
            false
        } else if (TextUtils.isEmpty(contact)) {
            user_contact.setFocusable(true)
            user_contact.setError("Contact should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Contact should not be empty", 1000).show()
            false
        } else if (contact.length < 11) {
            user_contact.setFocusable(true)
            user_contact.setError("Contact number should be at least 11 numbers")
            Snackbar.make(findViewById(android.R.id.content), "Contact number should be at least 12 numbers", 1000).show()
            false
        } else {
            true
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.update_btn -> {

            }
        }
    }

    fun postUpdateProfile(newName: String, newEmail: String, newNumber: String, userid: String) {
        utils?.showLoader(this)
        agsStore.postUpdateProfile(newName, newEmail, newNumber, userid, object : callback {
            override fun Success(response: String?) {
                utils?.hideLoader()
                val objects = JSONObject(response)
                val userid = objects["userid"].toString()
                if (!userid.equals("0", ignoreCase = true)) {
                    utils?.alertBox(this@UpdateProfile, "Congratulations!", "Your profile has been updated", "ok") { view, i ->
                        onBackPressed()
                        view.dismiss()
                    }
                }
            }

            override fun Failure(response: ErrorResponse?) {
                Toast.makeText(this@UpdateProfile, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                utils?.hideLoader()
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}

