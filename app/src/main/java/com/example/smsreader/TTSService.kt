package com.example.smsreader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

class TTSService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var speechText: String? = null
    private var isTtsReady = false

    override fun onCreate() {
        super.onCreate()
        Log.d("TTSService", "🟢 TTS engine initialized (onCreate)")
        tts = TextToSpeech(this, this)

        // Foreground notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("tts_channel", "TTS Notifications", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            val notification: Notification = NotificationCompat.Builder(this, "tts_channel")
                .setContentTitle("TTS Running")
                .setContentText("Speaking message...")
                .setSmallIcon(R.drawable.ic_payment)
                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        speechText = intent?.getStringExtra("SPEECH_TEXT")
        Log.d("TTSService", "🟢 Received speech text: $speechText")

        // If tts is ready, speak now
        if (isTtsReady && speechText != null) {
            speakOut()
        }

        return START_STICKY
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            isTtsReady = true

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTSService", "❌ Language not supported!")
            } else {
                // If text was already received, speak now
                if (speechText != null) {
                    speakOut()
                }
            }
        } else {
            Log.e("TTSService", "❌ TTS Initialization failed!")
        }
    }

    private fun speakOut() {
        Log.d("TTSService", "🎙 Speaking: $speechText")
        tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        Log.d("TTSService", "🛑 TTS shutdown")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
