package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo

class BRWifi(private val attachToActivity: ((Boolean) -> Unit)) : BroadcastReceiver() {

    fun getIntentFilter(): IntentFilter {
        return IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            attachToActivity?.let{
                attachToActivity(checkInternetAvailable(context))
            }
        }
    }

    private fun checkInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}