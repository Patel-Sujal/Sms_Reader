package com.example.smsreader


import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest // ✅ CORRECT
import android.content.Intent
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.zip.Inflater


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Reset Pref..
        val btnReset=findViewById<Button>(R.id.btnReset)
        btnReset.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent)

            Toast.makeText(this,"Reset Sucessfull...",Toast.LENGTH_SHORT).show()

        }

        //Shared Prefs for saving senders name
        val shredpref = this.getSharedPreferences("Myprefs", 0)
        val btnSave=findViewById<Button>(R.id.btnSavepref)
        val displayForsendersname=findViewById<TextView>(R.id.displaySendersName)

        fun senderRederer() {
            var sendername = shredpref.getString("smsrecipient", "")
            if (sendername==""){
                sendername="Notset Senders Name Please Add!!"
            }
            displayForsendersname.setText(sendername)
        }

        btnSave.setOnClickListener {
            val sender = findViewById<EditText>(R.id.txtboxSender)

            shredpref.edit()
                .apply {
                    putString("smsrecipient", sender.text.toString())
                    commit()
                }
            senderRederer()
        }

        senderRederer()




//Intents Initialize
        val intent = Intent(this, TTSService::class.java)
        intent.putExtra("message", "Hello, this is your TTS service speaking!")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent) // Required for Android 8+
        } else {
            startService(intent) // For older versions
        }


//Permission Box
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optonsmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
        R.id.Close->{
            val intent=Intent(this,TTSService::class.java)
            stopService(intent)
            finishAffinity()
            true
           }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
