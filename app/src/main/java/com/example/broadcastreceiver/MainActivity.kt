package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import android.os.Build
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var receiver: BroadcastReceiver
    private lateinit var tvBatteryPercent: TextView
    private lateinit var imgBattery: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBatteryPercent = findViewById(R.id.tvBatteryPercent)
        imgBattery = findViewById(R.id.imgBattery)
        val intent = IntentFilter()
        intent.addAction(Intent.ACTION_BATTERY_CHANGED)
        this.registerReceiver(batteryPercentReceiver, intent)

    }

    private val batteryPercentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val bm = context?.getSystemService(BATTERY_SERVICE) as BatteryManager
                    val percent =
                        bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toInt()
                    tvBatteryPercent.text = percent.toString() + "%"

                    val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                            || status == BatteryManager.BATTERY_STATUS_FULL

                    if (isCharging) {
                        imgBattery.setImageResource(R.drawable.battery_charged)
                    } else {
                        when (percent) {
                            in 0..5 -> imgBattery.setImageResource(R.drawable.battery_down)

                            in 6..25 -> imgBattery.setImageResource(R.drawable.battery_low)

                            in 26..50 -> imgBattery.setImageResource(R.drawable.battery_half)

                            in 51..75 -> imgBattery.setImageResource(R.drawable.battery_almost)

                            in 76..100 -> imgBattery.setImageResource(R.drawable.battery_full)
                        }
                    }
                }
                Intent.ACTION_BATTERY_LOW -> {
                }
                Intent.ACTION_BATTERY_OKAY -> {
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }


}