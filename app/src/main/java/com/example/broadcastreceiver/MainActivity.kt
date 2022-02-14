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
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var tvBatteryPercent: TextView
    private lateinit var imgBattery: ImageView
    private lateinit var imgWifi: ImageView
    private lateinit var brbattery: BRBattery
    private lateinit var brWifi: BRWifi


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)
        tvBatteryPercent = findViewById(R.id.tvBatteryPercent)
        imgBattery = findViewById(R.id.imgBattery)
        imgWifi = findViewById(R.id.imgWifi)

        brbattery = BRBattery { context, intent ->
            when (intent?.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    val bm = context?.getSystemService(BATTERY_SERVICE) as BatteryManager
                    val percent =
                        bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toInt()
                    tvBatteryPercent.text = "$percent%"

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

        brWifi = BRWifi { hasWifi ->
            if (hasWifi) {
                imgWifi.setImageResource(R.drawable.wifi)
            } else {
                imgWifi.setImageResource(R.drawable.no_wifi)
            }
        }

        this.registerReceiver(brbattery, brbattery.getIntentFilter())
        this.registerReceiver(brWifi, brWifi.getIntentFilter())
    }

    override fun onDestroy() {
        brbattery?.let {
            unregisterReceiver(brbattery)
        }
        brWifi?.let {
            unregisterReceiver(brWifi)
        }
        super.onDestroy()
    }


}