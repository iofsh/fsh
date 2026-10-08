// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.buffer

import io.fsh.shared.model.log.LogEntry

class RingBuffer(private val capacity: Int) {

    private val entries = ArrayDeque<LogEntry>(capacity)

    @Synchronized
    fun add(entry: LogEntry) {
        if (entries.size >= capacity) {
            entries.removeFirst()
        }
        entries.addLast(entry)
    }

    @Synchronized
    fun takeLast(count: Int): List<LogEntry> {
        val size = entries.size
        val take = if (count > size) size else count
        val result = mutableListOf<LogEntry>()
        var i = 0
        for (entry in entries) {
            if (i >= size - take) result += entry
            i++
        }
        return result
    }

    @Synchronized
    fun clear() {
        entries.clear()
    }

    @Synchronized
    fun size(): Int = entries.size
}
