// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class BatteryBinder(private val context: Context) {

    fun snapshot(): BatteryBinderSnapshot {
        val intent: Intent? = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        if (intent == null) {
            return BatteryBinderSnapshot(0, 0, 0, 0f, 0, "unknown")
        }
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
        val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

        return BatteryBinderSnapshot(
            levelPercent = level * 100 / scale,
            status = status,
            health = health,
            temperatureC = temp / 10f,
            voltageMv = voltage,
            technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "unknown",
        )
    }
}

data class BatteryBinderSnapshot(
    val levelPercent: Int,
    val status: Int,
    val health: Int,
    val temperatureC: Float,
    val voltageMv: Int,
    val technology: String,
)
