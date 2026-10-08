// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics.mem

import android.app.ActivityManager
import android.content.Context

class MemCollector(private val context: Context) {

    fun collect(): MemSnapshot {
        return try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = ActivityManager.MemoryInfo()
            am.getMemoryInfo(info)
            MemSnapshot(
                totalBytes = info.totalMem,
                availableBytes = info.availMem,
                usedBytes = info.totalMem - info.availMem,
            )
        } catch (_: Throwable) {
            MemSnapshot(0, 0, 0)
        }
    }
}

data class MemSnapshot(
    val totalBytes: Long,
    val availableBytes: Long,
    val usedBytes: Long,
)
