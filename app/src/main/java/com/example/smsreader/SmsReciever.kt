package com.example.smsreader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.PowerManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {





    override fun onReceive(context: Context, intent: Intent) {

            val shred=context.getSharedPreferences("Myprefs",0)
       val sender= shred.getString("smsrecipient","")
Toast.makeText(context,"$sender",Toast.LENGTH_SHORT).show()


         val allowedSenders = setOf("$sender")

        val bundle = intent.extras
        val pdus = bundle?.get("pdus") as? Array<*>

        pdus?.forEach { pdu ->
            val sms = SmsMessage.createFromPdu(pdu as ByteArray)
            val sender = sms.displayOriginatingAddress
            val message = sms.messageBody

            if (!allowedSenders.contains(sender)) {
                Log.d("SMSReceiver", "❌ Ignored SMS from: $sender")
                return
            }

            if (!message.contains("credited", ignoreCase = true)) {
                Log.d("SMSReceiver", "❌ Message does not contain 'credited'")
                return
            }

            val extractedAmount = extractAmount(message)

            if (extractedAmount != null) {
                val speechText = "You. have. received. $extractedAmount . rupees"

                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "SmsReader::WakeLock"
                )
                wakeLock.acquire(5000)

                val serviceIntent = Intent(context, TTSService::class.java).apply {
                    putExtra("SPEECH_TEXT", speechText)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }

                Log.d("SMSReceiver", "✅ Spoken: $speechText")
            } else {
                Log.d("SMSReceiver", "❌ Could not extract amount.")
            }
        }
    }

    private fun extractAmount(message: String): Int? {
        val pattern = Pattern.compile("Rs\\.?\\s*(\\d+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(message)
        return if (matcher.find()) {
            val amountString = matcher.group(1)
            amountString?.toDoubleOrNull()?.toInt() // Convert to Int directly
        } else {
            null
        }
    }
}
