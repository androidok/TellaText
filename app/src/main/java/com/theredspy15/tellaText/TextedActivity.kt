package com.theredspy15.tellaText

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.theredspy15.tellaText.Utils.Companion.contactExists
import com.theredspy15.tellaText.Utils.Companion.getContactName


class TextedActivity : AppCompatActivity() {

    private var smsSender = ""
    private var smsMessage = ""
    private var smsSenderName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texted)

        smsSender = getString(R.string.unknown)
        smsMessage = getString(R.string.nothing_found)

        smsSenderName = if (contactExists(this, intent.getStringExtra("sms_sender")))
            getContactName(intent.getStringExtra("sms_sender"), this)
        else ""
        smsSender = intent.getStringExtra("sms_sender")
        smsMessage = intent.getStringExtra("sms_body")

        val senderTxtView: TextView = findViewById(R.id.txt_sender)
        if (smsSenderName != "") senderTxtView.text = smsSenderName
        else senderTxtView.text = smsSender

        Utils.vibrate(this)
    }

    fun ignoreMSG(view: View) {
        finish()
    }

    fun speakMSG(view: View) {
        Utils.speak(this, smsMessage)
    }

    fun respond(view: View) {
        val textedIntent = Intent(this, RespondActivity::class.java)
        textedIntent.putExtra("sms_body", smsMessage)
        textedIntent.putExtra("sms_sender", smsSender)
        textedIntent.putExtra("sms_sender_name", smsSenderName)

        ContextCompat.startActivity(this, textedIntent, null)
    }
}
