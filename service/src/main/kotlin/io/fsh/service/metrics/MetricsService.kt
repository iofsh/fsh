// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics

import android.content.Context
import io.fsh.service.metrics.battery.BatteryCollector
import io.fsh.service.metrics.cpu.CpuCollector
import io.fsh.service.metrics.mem.MemCollector
import io.fsh.service.metrics.net.NetCollector
import io.fsh.service.metrics.thermal.ThermalCollector

class MetricsService(context: Context) {

    private val cpu = CpuCollector()
    private val mem = MemCollector(context)
    private val net = NetCollector(context)
    private val thermal = ThermalCollector()
    private val battery = BatteryCollector(context)

    fun snapshot(): MetricsSnapshot = MetricsSnapshot(
        cpu = cpu.collect(),
        mem = mem.collect(),
        net = net.collect(),
        thermal = thermal.collect(),
        battery = battery.collect(),
    )
}

data class MetricsSnapshot(
    val cpu: CpuSnapshot,
    val mem: MemSnapshot,
    val net: NetSnapshot,
    val thermal: ThermalSnapshot,
    val battery: BatterySnapshot,
)
