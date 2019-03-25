package com.example.tellaText

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.core.content.ContextCompat.startActivity

class SmsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val textedIntent = Intent(context, TextedActivity::class.java)
                textedIntent.putExtra("sms_body", smsMessage.messageBody)
                textedIntent.putExtra("sms_sender", smsMessage.originatingAddress)
                textedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(context, textedIntent, null)
            }
        }
    }
}
