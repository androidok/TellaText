package com.example.tellaText

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.provider.ContactsContract.PhoneLookup
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class TextedActivity : AppCompatActivity() {

    private var smsSender = "Unknown"
    private var smsMessage = "No text found"
    private var textToSpeechSystem: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texted)

        if (contactExists(this, intent.getStringExtra("sms_sender"))) {
            smsSender = getContactName(intent.getStringExtra("sms_sender"))
        }
        smsMessage = intent.getStringExtra("sms_body")

        val senderTxtView: TextView = findViewById(R.id.txt_sender)
        senderTxtView.text = smsSender
    }

    fun callSender(view: View) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$smsSender")
        startActivity(intent)
    }

    fun ignoreMSG(view: View) {
        finish()
    }

    fun speakMSG(view: View) {
        textToSpeechSystem = TextToSpeech(this) { ttsInitResult ->
            if (TextToSpeech.SUCCESS == ttsInitResult) textToSpeechSystem!!.speak(smsMessage, TextToSpeech.QUEUE_ADD, null)
        }
    }

    private fun contactExists(context: Context, number: String): Boolean {
        val lookupUri = Uri.withAppendedPath(
            PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val mPhoneNumberProjection = arrayOf(PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME)
        val cursor = context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
        cursor.use { cur ->
            if (cur!!.moveToFirst()) {
                return true
            }
        }
        return false
    }

    private fun getContactName(number: String): String {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        var name = "?"

        val contentResolver = contentResolver
        val lookup = contentResolver.query(
            uri,
            arrayOf(BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        )

        lookup.use { contactLookup ->
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
            }
        }

        return name
    }

}
