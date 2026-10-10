// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.screen.summary

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fsh.data.backend.BackendManager
import io.fsh.data.backend.BackendState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class SummaryStats(
    val cpuPercent: Float = 0f,
    val memUsed: Long = 0L,
    val memTotal: Long = 0L,
    val batteryPercent: Int = 0,
    val batteryTemp: Float = 0f,
    val storageUsed: Long = 0L,
    val storageTotal: Long = 0L,
    val processCount: Int = 0,
    val threadCount: Int = 0,
    val uptimeMs: Long = 0L,
)

@HiltViewModel
class SummaryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val backend: BackendManager,
) : ViewModel() {

    private val _stats = MutableStateFlow(SummaryStats())
    val stats: StateFlow<SummaryStats> = _stats.asStateFlow()

    val backendState: StateFlow<BackendState> = backend.state

    private var lastCpuTotal = 0L
    private var lastCpuIdle = 0L

    init {
        viewModelScope.launch {
            while (true) {
                _stats.value = collect()
                delay(2_000)
            }
        }
    }

    private fun collect(): SummaryStats {
        val cpu = readCpu()

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)

        val battery = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = battery?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
        val scale = battery?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
        val temp = battery?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0

        val stat = StatFs(Environment.getDataDirectory().absolutePath)

        var procs = 0
        var threads = 0
        File("/proc").listFiles()?.forEach { e ->
            val pid = e.name.toIntOrNull() ?: return@forEach
            procs++
            runCatching {
                File("/proc/$pid/status").forEachLine { line ->
                    if (line.startsWith("Threads:")) {
                        threads += line.substringAfter(":").trim().toIntOrNull() ?: 0
                    }
                }
            }
        }

        return SummaryStats(
            cpuPercent = cpu,
            memUsed = mi.totalMem - mi.availMem,
            memTotal = mi.totalMem,
            batteryPercent = if (scale > 0) level * 100 / scale else 0,
            batteryTemp = temp / 10f,
            storageUsed = stat.totalBytes - stat.availableBytes,
            storageTotal = stat.totalBytes,
            processCount = procs,
            threadCount = threads,
            uptimeMs = SystemClock.elapsedRealtime(),
        )
    }

    private fun readCpu(): Float {
        val line = File("/proc/stat").readLines().firstOrNull { it.startsWith("cpu ") } ?: return 0f
        val parts = line.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (parts.size < 5) return 0f
        val total = parts.drop(1).sumOf { it.toLongOrNull() ?: 0L }
        val idle = parts.getOrNull(4)?.toLongOrNull() ?: 0L
        val dTotal = total - lastCpuTotal
        val dIdle = idle - lastCpuIdle
        lastCpuTotal = total
        lastCpuIdle = idle
        if (dTotal <= 0) return 0f
        return ((dTotal - dIdle).toFloat() / dTotal) * 100f
    }
}
