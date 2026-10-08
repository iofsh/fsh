// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.parser

import io.fsh.service.proc.status.Status

class StatusParser {

    fun parse(raw: String): Status? {
        return try {
            val uid = Regex("Uid:\\s+(\\d+)").find(raw)?.groupValues?.get(1)?.toIntOrNull()
                ?: return null
            val name = Regex("Name:\\s+(.+)").find(raw)?.groupValues?.get(1)?.trim()
                ?: "?"
            val pid = Regex("Pid:\\s+(\\d+)").find(raw)?.groupValues?.get(1)?.toIntOrNull()
                ?: 0
            val rss = Regex("VmRSS:\\s+(\\d+)").find(raw)?.groupValues?.get(1)?.toLongOrNull()
                ?: 0L
            val ppid = Regex("PPid:\\s+(\\d+)").find(raw)?.groupValues?.get(1)?.toIntOrNull()
                ?: 0
            val threads = Regex("Threads:\\s+(\\d+)").find(raw)?.groupValues?.get(1)?.toIntOrNull()
                ?: 0

            Status(
                pid = pid,
                uid = uid,
                ppid = ppid,
                name = name,
                rssKb = rss,
                threads = threads,
            )
        } catch (_: Throwable) {
            null
        }
    }
}
