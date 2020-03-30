package com.theredspy15.tellaText

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.Telephony
import androidx.core.content.ContextCompat.startActivity

class SmsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("active", false)) {
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
