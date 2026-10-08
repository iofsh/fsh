// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.buffer

import io.fsh.shared.model.log.LogEntry

class Scrollback(private val maxLines: Int = 10_000) {

    private val lines = ArrayDeque<LogEntry>(maxLines)

    @Synchronized
    fun append(entry: LogEntry) {
        if (lines.size >= maxLines) lines.removeFirst()
        lines.addLast(entry)
    }

    @Synchronized
    fun snapshot(): List<LogEntry> = lines.toList()

    @Synchronized
    fun clear() = lines.clear()
}
