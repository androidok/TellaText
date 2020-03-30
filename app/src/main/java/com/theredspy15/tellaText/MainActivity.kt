package com.theredspy15.tellaText

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences.Editor

    private var statusTxtView: TextView? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = PreferenceManager.getDefaultSharedPreferences(this).edit()!!

        statusTxtView = findViewById(R.id.txt_status)

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("active", false)) {
            statusTxtView!!.text = getString(R.string.status_enabled)
            statusTxtView!!.setTextColor(Color.parseColor("#00C853"))
        } else {
            statusTxtView!!.text = getString(R.string.status_disabled)
            statusTxtView!!.setTextColor(Color.parseColor("#DD2C00"))
        }

        requestPermissions()
    }

    fun enableMonitor(view: View) {
        prefs.putBoolean("active", true)
        prefs.commit()

        statusTxtView!!.text = getString(R.string.status_enabled)
        statusTxtView!!.setTextColor(Color.parseColor("#00C853"))

        Utils.vibrate(this)
    }

    fun disableMonitor(view: View) {
        prefs.putBoolean("active", false)
        prefs.commit()

        statusTxtView!!.text = getString(R.string.status_disabled)
        statusTxtView!!.setTextColor(Color.parseColor("#DD2C00"))

        Utils.vibrate(this)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS),
            101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, getString(R.string.warning_permission), Toast.LENGTH_LONG).show() // permission denied
        }
    }
}
