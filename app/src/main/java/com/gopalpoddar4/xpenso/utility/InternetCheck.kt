package com.gopalpoddar4.xpenso.utility

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.gopalpoddar4.xpenso.R

object InternetCheck {
    fun isInternetAvailable(fraagment: Fragment): Boolean{
        val context = fraagment.requireContext()
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun showEnableInternetDialog(fragment: Fragment){
        val context = fragment.requireContext()

        val view = LayoutInflater.from(context).inflate(R.layout.alert_dailog_layout,null)
        val builder = AlertDialog.Builder(context, com.google.android.material.R.style.ThemeOverlay_Material3_Dialog)
        builder.setView(view)
        builder.setCancelable(false)
        val dailog = builder.create()

        val openSettingsBtn = view.findViewById<TextView>(R.id.openSettingsBtn)

        openSettingsBtn.setOnClickListener {
            dailog.dismiss()
            try {
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ => Internet connectivity panel (Wi-Fi/Mobile Data toggle)
                    Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                } else {
                    // Older Android => Wireless settings (Network & Internet)
                    Intent(Settings.ACTION_WIRELESS_SETTINGS)
                }
                fragment.startActivity(intent) // Use from Fragment/Activity
            } catch (e: Exception) {
                // Fallback (rare)
                try {
                    fragment.startActivity(Intent(Settings.ACTION_SETTINGS))
                } catch (ex: Exception) {
                    Toast.makeText(context, "Unable to open Settings", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dailog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dailog.show()
    }
}