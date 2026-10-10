// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.shared.model.log

enum class LogLevel(val priority: Int, val label: String) {
    VERBOSE(2, "V"),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARN(5, "W"),
    ERROR(6, "E"),
    FATAL(7, "F"),
    UNKNOWN(0, "?");

    companion object {
        fun fromPriority(p: Int): LogLevel =
            entries.firstOrNull { it.priority == p } ?: UNKNOWN
    }
}
