// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.meminfo

import android.app.ActivityManager
import android.content.Context

class MemInfoBinder(private val context: Context) {

    private val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    fun snapshot(): MemInfoSnapshot {
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        return MemInfoSnapshot(
            totalBytes = info.totalMem,
            availableBytes = info.availMem,
            thresholdBytes = info.threshold,
            lowMemory = info.lowMemory,
        )
    }
}

data class MemInfoSnapshot(
    val totalBytes: Long,
    val availableBytes: Long,
    val thresholdBytes: Long,
    val lowMemory: Boolean,
)
