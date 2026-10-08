// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc

import io.fsh.service.aidl.IProcessService
import io.fsh.service.aidl.ProcessInfo
import io.fsh.service.proc.parser.StatParser
import io.fsh.service.proc.parser.StatusParser
import io.fsh.service.proc.reader.ProcReader
import java.io.File

class ProcessServiceImpl : IProcessService.Stub() {

    private val reader = ProcReader()
    private val statusParser = StatusParser()
    private val statParser = StatParser()

    override fun getProcesses(): List<ProcessInfo> {
        val result = mutableListOf<ProcessInfo>()
        val entries = File("/proc").listFiles() ?: return result
        for (entry in entries) {
            val pid = entry.name.toIntOrNull() ?: continue
            val info = readProcess(pid) ?: continue
            result += info
        }
        return result
    }

    override fun getProcess(pid: Int): ProcessInfo? = readProcess(pid)

    override fun killProcess(pid: Int): Boolean {
        return try {
            android.os.Process.killProcess(pid)
            true
        } catch (_: Throwable) {
            false
        }
    }

    override fun forceStop(packageName: String, userId: Int): Boolean {
        return try {
            val result = reader.shell("am force-stop --user $userId $packageName")
            result.isEmpty()
        } catch (_: Throwable) {
            false
        }
    }

    override fun getTotalMemory(): Long = reader.readMemInfo("MemTotal")

    override fun getAvailableMemory(): Long = reader.readMemInfo("MemAvailable")

    private fun readProcess(pid: Int): ProcessInfo? {
        return try {
            val status = reader.readStatus(pid) ?: return null
            val stat = reader.readStat(pid)
            val parsed = statusParser.parse(status) ?: return null
            val cpu = if (stat != null) statParser.cpuPercent(stat) else 0f

            ProcessInfo(
                pid = parsed.pid,
                uid = parsed.uid,
                name = parsed.name,
                packageName = null,
                rssKb = parsed.rssKb,
                pssKb = 0L,
                cpuPercent = cpu,
            )
        } catch (_: Throwable) {
            null
        }
    }
}
