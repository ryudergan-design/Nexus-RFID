package com.example.nexusrfid.rfid

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object RfidPermissionGateway {
    fun requiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun missingPermissions(context: Context): Array<String> {
        return requiredPermissions()
            .filter { permission ->
                ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
            }
            .toTypedArray()
    }

    fun hasRequiredPermissions(context: Context): Boolean {
        return missingPermissions(context).isEmpty()
    }

    fun isBluetoothEnabled(context: Context): Boolean {
        val manager = context.getSystemService(BluetoothManager::class.java)
        return manager?.adapter?.isEnabled == true
    }

    fun bluetoothEnableIntent(): Intent {
        return Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    }
}

