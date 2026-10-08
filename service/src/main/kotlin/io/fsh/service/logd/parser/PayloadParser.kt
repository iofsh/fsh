// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.parser

import io.fsh.shared.model.log.LogEntry

class PayloadParser {

    fun parse(payload: ByteArray, pid: Int, tid: Int, sec: Int, nsec: Int, uid: Int): LogEntry? {
        if (payload.isEmpty()) return null
        val priority = payload[0].toInt()
        var tagEnd = 1
        while (tagEnd < payload.size && payload[tagEnd] != 0.toByte()) tagEnd++
        if (tagEnd >= payload.size) return null

        val tag = String(payload, 1, tagEnd - 1, Charsets.UTF_8)
        val messageStart = tagEnd + 1
        val message = if (messageStart < payload.size) {
            String(payload, messageStart, payload.size - messageStart, Charsets.UTF_8)
        } else ""

        val timestamp = sec * 1000L + nsec / 1_000_000L

        return LogEntry(
            timestamp = timestamp,
            pid = pid,
            tid = tid,
            priority = priority,
            tag = tag,
            message = message,
        )
    }
}
