package com.example.tellaText

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
    }
}