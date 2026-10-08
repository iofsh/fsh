// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.reader

import java.io.File

class ProcReader {

    fun readStatus(pid: Int): String? {
        return try {
            File("/proc/$pid/status").takeIf { it.exists() }?.readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun readStat(pid: Int): String? {
        return try {
            File("/proc/$pid/stat").takeIf { it.exists() }?.readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun readCmdline(pid: Int): String? {
        return try {
            val file = File("/proc/$pid/cmdline")
            if (!file.exists()) return null
            file.readBytes().toString(Charsets.UTF_8).replace('\u0000', ' ').trim()
        } catch (_: Throwable) {
            null
        }
    }

    fun readMaps(pid: Int): String? {
        return try {
            File("/proc/$pid/maps").takeIf { it.exists() }?.readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun readSmapsRollup(pid: Int): String? {
        return try {
            File("/proc/$pid/smaps_rollup").takeIf { it.exists() }?.readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun readOomScoreAdj(pid: Int): Int {
        return try {
            File("/proc/$pid/oom_score_adj").readText().trim().toIntOrNull() ?: 0
        } catch (_: Throwable) {
            0
        }
    }

    fun readCgroup(pid: Int): String? {
        return try {
            File("/proc/$pid/cgroup").takeIf { it.exists() }?.readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun readMemInfo(key: String): Long {
        return try {
            val lines = File("/proc/meminfo").readLines()
            for (line in lines) {
                if (line.startsWith(key)) {
                    return Regex("(\\d+)").find(line)?.groupValues?.get(1)?.toLongOrNull() ?: 0L
                }
            }
            0L
        } catch (_: Throwable) {
            0L
        }
    }

    fun readProcStat(): String? {
        return try {
            File("/proc/stat").readText()
        } catch (_: Throwable) {
            null
        }
    }

    fun shell(command: String): String {
        return try {
            val process = ProcessBuilder("sh", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (_: Throwable) {
            ""
        }
    }
}
