// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.activity

import android.app.ActivityManager
import android.content.Context

class ProcessBinder(private val context: Context) {

    private val am: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    fun runningProcesses(): List<ActivityManager.RunningAppProcessInfo> =
        am.runningAppProcesses ?: emptyList()

    fun memoryFor(pids: IntArray): Array<ActivityManager.MemoryInfo> = emptyArray()
}
