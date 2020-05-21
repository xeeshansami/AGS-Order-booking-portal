package com.ags.agssalesandroidclientorder.Activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ags.agssalesandroidclientorder.Database.DatabaseHandler
import com.ags.agssalesandroidclientorder.Models.EntityCustomer
import com.ags.agssalesandroidclientorder.Network.model.response.ErrorResponse
import com.ags.agssalesandroidclientorder.Network.responseHandler.callbacks.callback
import com.ags.agssalesandroidclientorder.Network.store.AGSStore
import com.ags.agssalesandroidclientorder.R
import com.ags.agssalesandroidclientorder.Utils.SharedPreferenceHandler
import com.ags.agssalesandroidclientorder.Utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_update_customer_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.toolbar
import kotlinx.android.synthetic.main.activity_update_profile.update_btn
import kotlinx.android.synthetic.main.activity_update_profile.user_email
import kotlinx.android.synthetic.main.activity_update_profile.user_name
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class UpdateCustomerProfile : AppCompatActivity(), View.OnClickListener {
    var db: DatabaseHandler? = null
    var selectedCustomer: EntityCustomer? = null
    val myCalendar = Calendar.getInstance()
    var agsStore: AGSStore = AGSStore.getInstance()
    var utils: Utils? = null
    var sp: SharedPreferenceHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer_profile)
        toolbar.subtitle = "Update Customer Profile"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_app_24dp)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorPrimary))
        toolbar.setSubtitleTextColor(resources.getColor(R.color.colorPrimary))
        toolbar.setNavigationOnClickListener { finish() }
        sp = SharedPreferenceHandler(this)
        db = DatabaseHandler(this)
        utils = Utils(this)
        /*sp!!.getusername().toString()?.let { login_id.setText(it) }
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
        }*/

        customer_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var datePicker = DatePickerDialog(this@UpdateCustomerProfile, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                        myCalendar[Calendar.DAY_OF_MONTH])
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
                datePicker.show()
            }
        })
        update_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (validation()) {
                    postUpdateProfile(btnSelectCustomer.text.toString().trim(),
                            customer_address.text.toString().trim(),
                            customer_licences.text.toString().trim()
                            , customer_date.text.toString().trim()
                            , customer_location.text.toString().trim(),customer_location.text.toString().trim(),
                            customer_contact_person.text.toString().trim(), sp!!.getuserid())
                }
            }
        })
    }

    var date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        myCalendar.add(Calendar.DATE, 0);
        // Set the Calendar new date as minimum date of date picker
        updateLabel()
    }

    fun SelectCustomer(v: View?) {
        val intent = Intent(this@UpdateCustomerProfile, CustomerActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun updateLabel() {
        val myFormat = "dd-MMM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        customer_date.setText(sdf.format(myCalendar.time))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                selectedCustomer = EntityCustomer()
                val customerId = data?.getStringExtra("customerId")?.toInt()
                selectedCustomer = customerId?.let { db?.getCustomer(it) }
                btnSelectCustomer.setText(selectedCustomer?.getCustomerName() + "\n" + selectedCustomer?.customerAddress)
                customer_address.setText(selectedCustomer?.customerAddress)
                customer_licences.setText(selectedCustomer?.customerBranch)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    fun validation(): Boolean {
        val name: String = btnSelectCustomer.getText().toString().trim({ it <= ' ' })
        val address: String = customer_address.getText().toString().trim({ it <= ' ' })
        val licence: String = customer_licences.getText().toString().trim({ it <= ' ' })
        val contact: String = customer_contact_person.getText().toString().trim({ it <= ' ' })
        if (name.equals("Select Customer", ignoreCase = true)) {
            btnSelectCustomer.setFocusable(true)
            btnSelectCustomer.setError("Please select an customer")
            Snackbar.make(findViewById(android.R.id.content), "Please select an customer", 1000).show()
           return false
        } else if (TextUtils.isEmpty(address)) {
            customer_address.setFocusable(true)
            customer_address.setError("Address should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Address should not be empty", 1000).show()
            return false
        } else if (TextUtils.isEmpty(licence)) {
            customer_licences.setFocusable(true)
            customer_licences.setError("Licence should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Licence should not be empty", 1000).show()
            return   false
        } else if (licence.length < 4) {
            customer_licences.setFocusable(true)
            customer_licences.setError("Licence number should be at least 4 numbers")
            Snackbar.make(findViewById(android.R.id.content), "Licence number should be at least 4 numbers", 1000).show()
            return   false
        } else if (TextUtils.isEmpty(contact)) {
            customer_contact_person.setFocusable(true)
            customer_contact_person.setError("Contact should not be empty")
            Snackbar.make(findViewById(android.R.id.content), "Contact should not be empty", 1000).show()
            return   false
        } else if (contact.length < 11) {
            customer_contact_person.setFocusable(true)
            customer_contact_person.setError("Contact number should be at least 11 numbers")
            Snackbar.make(findViewById(android.R.id.content), "Contact number should be at least 11 numbers", 1000).show()
            return  false
        } else {
            return    true
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

    fun postUpdateProfile(customerName: String, customerAddress: String, customerLicence: String, customerDate: String, vLat: String, vLong: String, customerContactPerson: String, userId: String) {
        utils?.showLoader(this)
        agsStore.postUpdateProfileForCustomer(customerName, vLat, vLong, customerLicence, customerDate, customerContactPerson, customerAddress, userId, object : callback {
            override fun Success(response: String?) {
                utils?.hideLoader()
                val objects = JSONObject(response)
                val userid = objects["userid"].toString()
                if (!userid.equals("0", ignoreCase = true)) {
                    utils?.alertBox(this@UpdateCustomerProfile, "Congratulations!", "Your profile has been updated", "ok") { view, i ->
                        onBackPressed()
                        view.dismiss()
                    }
                }
            }

            override fun Failure(response: ErrorResponse?) {
                Toast.makeText(this@UpdateCustomerProfile, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                utils?.hideLoader()
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}

