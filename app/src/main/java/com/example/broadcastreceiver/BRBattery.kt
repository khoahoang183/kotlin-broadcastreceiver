package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BRBattery(private val attachToActivity: ((Context, Intent?) -> Unit)) : BroadcastReceiver() {

    fun getIntentFilter(): IntentFilter {
        return IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.let {
                attachToActivity.let {
                    attachToActivity.invoke(context, intent)
                }
            }
        }

    }
}