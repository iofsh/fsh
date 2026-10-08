// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.parser

import io.fsh.service.proc.stat.Stat

class StatParser {

    private val previous = mutableMapOf<Int, Long>()
    private var previousTotal = 0L

    fun parse(raw: String): Stat? {
        return try {
            val tokens = raw.split(" ")
            if (tokens.size < 15) return null
            val pid = tokens[0].toIntOrNull() ?: return null
            val utime = tokens[13].toLongOrNull() ?: 0L
            val stime = tokens[14].toLongOrNull() ?: 0L
            Stat(pid = pid, utime = utime, stime = stime)
        } catch (_: Throwable) {
            null
        }
    }

    fun cpuPercent(raw: String): Float {
        val stat = parse(raw) ?: return 0f
        val total = stat.utime + stat.stime
        val prev = previous[stat.pid]
        previous[stat.pid] = total
        if (prev == null) return 0f

        val delta = total - prev
        val totalTicks = readTotalTicks()
        val deltaTotal = totalTicks - previousTotal
        previousTotal = totalTicks
        if (deltaTotal <= 0) return 0f

        return (delta.toFloat() / deltaTotal.toFloat()) * 100f
    }

    private fun readTotalTicks(): Long {
        return try {
            val line = java.io.File("/proc/stat").readLines().firstOrNull { it.startsWith("cpu ") }
                ?: return 0L
            val parts = line.split(" ").filter { it.isNotBlank() }
            var sum = 0L
            for (i in 1 until parts.size) {
                sum += parts[i].toLongOrNull() ?: 0L
            }
            sum
        } catch (_: Throwable) {
            0L
        }
    }
}
