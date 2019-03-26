package com.example.tellaText

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    @SuppressLint("CommitPrefEdits")
    private val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()!!

    var statusTxtView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTxtView = findViewById(R.id.txt_status)

        requestPermissions()
    }

    fun enableMonitor(view: View) {
        editor.putBoolean("active", true)
        editor.commit()

        statusTxtView!!.text = getString(R.string.status_enabled)
        statusTxtView!!.setTextColor(Color.parseColor("#00C853"))
    }

    fun disableMonitor(view: View) {
        editor.putBoolean("active", false)
        editor.commit()

        statusTxtView!!.text = getString(R.string.status_disabled)
        statusTxtView!!.setTextColor(Color.parseColor("#DD2C00"))
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE),
            101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, getString(R.string.warning_permission), Toast.LENGTH_LONG).show() // permission denied
        }
    }
}
