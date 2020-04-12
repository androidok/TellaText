package com.theredspy15.tellaText

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class RespondActivity : AppCompatActivity() {

    private var smsSender = ""
    private var smsMessage = ""
    private var smsSenderName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respond)

        smsSender = intent.getStringExtra("sms_sender")
        smsSenderName = intent.getStringExtra("sms_sender_name")
        smsMessage = intent.getStringExtra("sms_body")

        val senderTxtView: TextView = findViewById(R.id.txt_sender)
        if (smsSenderName != "") senderTxtView.text = smsSenderName
        else senderTxtView.text = smsSender
    }

    fun ignoreMSG(view: View) {
        finish()
    }

    fun textSender(view: View) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:$smsSender")
        startActivity(sendIntent);
    }

    fun callSender(view: View) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$smsSender")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        startActivity(intent)
    }
}
