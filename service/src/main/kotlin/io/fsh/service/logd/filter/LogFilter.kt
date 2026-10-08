// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.filter

import io.fsh.shared.model.log.LogEntry

data class LogFilter(
    val minPriority: Int = 0,
    val pids: Set<Int> = emptySet(),
    val tags: Set<String> = emptySet(),
    val searchText: String? = null,
) {
    fun matches(entry: LogEntry): Boolean {
        if (entry.priority < minPriority) return false
        if (pids.isNotEmpty() && entry.pid !in pids) return false
        if (tags.isNotEmpty() && entry.tag !in tags) return false
        if (searchText != null && !entry.message.contains(searchText, ignoreCase = true)) return false
        return true
    }
}
