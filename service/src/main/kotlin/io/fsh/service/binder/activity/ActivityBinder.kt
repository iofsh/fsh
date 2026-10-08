// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.activity

import android.app.ActivityManager
import android.content.Context

class ActivityBinder(private val context: Context) {

    private val am: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    fun memoryInfo(): ActivityManager.MemoryInfo {
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        return info
    }
}
