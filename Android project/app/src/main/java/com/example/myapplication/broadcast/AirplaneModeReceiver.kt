package com.example.myapplication.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            val message = if (isAirplaneModeOn) "Airplane Mode ON ✈️" else "Airplane Mode OFF ✅"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
