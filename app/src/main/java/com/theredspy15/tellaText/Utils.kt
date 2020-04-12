package com.theredspy15.tellaText

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.speech.tts.TextToSpeech
import com.sdsmdg.tastytoast.TastyToast


class Utils {
    companion object {
        private var textToSpeechSystem: TextToSpeech? = null

        fun vibrate(context: Context) {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(500)
            }
        }

        fun speak(context: Context, smsMessage: String) = if (smsMessage.length < TextToSpeech.getMaxSpeechInputLength()) {
            textToSpeechSystem = TextToSpeech(context) { ttsInitResult ->
                if (TextToSpeech.SUCCESS == ttsInitResult) {
                    vibrate(context)
                    TastyToast.makeText(context, context.getString(R.string.playing), TastyToast.LENGTH_LONG, TastyToast.DEFAULT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeechSystem!!.speak(smsMessage, TextToSpeech.QUEUE_ADD, null, "tellatext")
                    } else {
                        textToSpeechSystem!!.speak(smsMessage, TextToSpeech.QUEUE_ADD, null)
                    }
                }
            }
        } else TastyToast.makeText(context, context.getString(R.string.msg_too_long), TastyToast.LENGTH_LONG, TastyToast.ERROR).show()

        fun getContactName(number: String, context: Context): String {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            var name = "?"

            val contentResolver = context.contentResolver
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

        fun contactExists(context: Context, number: String): Boolean {
            val lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number)
            )
            val mPhoneNumberProjection = arrayOf(ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
            cursor.use { cur ->
                if (cur!!.moveToFirst()) {
                    return true
                }
            }
            return false
        }
    }
}