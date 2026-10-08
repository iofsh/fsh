// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics.cpu

import java.io.File

class CpuCollector {

    private var lastTotal = 0L
    private var lastIdle = 0L

    fun collect(): CpuSnapshot {
        return try {
            val line = File("/proc/stat").readLines().firstOrNull { it.startsWith("cpu ") }
                ?: return CpuSnapshot(0f, 0L, 0L)
            val parts = line.split(" ").filter { it.isNotBlank() }
            if (parts.size < 5) return CpuSnapshot(0f, 0L, 0L)

            val user = parts[1].toLongOrNull() ?: 0L
            val nice = parts[2].toLongOrNull() ?: 0L
            val system = parts[3].toLongOrNull() ?: 0L
            val idle = parts[4].toLongOrNull() ?: 0L
            val iowait = parts.getOrNull(5)?.toLongOrNull() ?: 0L
            val irq = parts.getOrNull(6)?.toLongOrNull() ?: 0L
            val softirq = parts.getOrNull(7)?.toLongOrNull() ?: 0L

            val total = user + nice + system + idle + iowait + irq + softirq
            val deltaTotal = total - lastTotal
            val deltaIdle = idle - lastIdle
            lastTotal = total
            lastIdle = idle

            val usage = if (deltaTotal > 0) {
                ((deltaTotal - deltaIdle).toFloat() / deltaTotal.toFloat()) * 100f
            } else 0f

            CpuSnapshot(
                usagePercent = usage,
                totalTicks = total,
                idleTicks = idle,
            )
        } catch (_: Throwable) {
            CpuSnapshot(0f, 0L, 0L)
        }
    }
}

data class CpuSnapshot(
    val usagePercent: Float,
    val totalTicks: Long,
    val idleTicks: Long,
)
