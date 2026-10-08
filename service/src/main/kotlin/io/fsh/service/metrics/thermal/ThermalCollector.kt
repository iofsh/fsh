// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics.thermal

import java.io.File

class ThermalCollector {

    fun collect(): ThermalSnapshot {
        val zones = mutableMapOf<String, Float>()
        try {
            val typeDir = File("/sys/class/thermal")
            val entries = typeDir.listFiles() ?: return ThermalSnapshot(zones)
            for (entry in entries) {
                if (!entry.name.startsWith("thermal_zone")) continue
                val tempFile = File(entry, "temp")
                val typeFile = File(entry, "type")
                if (!tempFile.exists()) continue
                val temp = tempFile.readText().trim().toFloatOrNull() ?: continue
                val type = if (typeFile.exists()) typeFile.readText().trim() else entry.name
                zones[type] = temp / 1000f
            }
        } catch (_: Throwable) {
        }
        return ThermalSnapshot(zones)
    }
}

data class ThermalSnapshot(
    val zones: Map<String, Float>,
)
