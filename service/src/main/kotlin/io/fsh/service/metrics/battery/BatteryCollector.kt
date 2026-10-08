// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class BatteryCollector(private val context: Context) {

    fun collect(): BatteryCollectorSnapshot {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        if (intent == null) return BatteryCollectorSnapshot(0, 0f, 0, false, 0)

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
        val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        return BatteryCollectorSnapshot(
            levelPercent = if (scale > 0) level * 100 / scale else 0,
            temperatureC = temp / 10f,
            voltageMv = voltage,
            charging = charging,
            status = status,
        )
    }
}

data class BatteryCollectorSnapshot(
    val levelPercent: Int,
    val temperatureC: Float,
    val voltageMv: Int,
    val charging: Boolean,
    val status: Int,
)
