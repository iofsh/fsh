// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.parser

import io.fsh.service.proc.smaps.Smaps

class SmapsParser {

    fun parse(raw: String): Smaps {
        var pss = 0L
        var rss = 0L
        var shared = 0L
        var private = 0L

        for (line in raw.lines()) {
            when {
                line.startsWith("Pss:") -> pss += extract(line)
                line.startsWith("Rss:") -> rss += extract(line)
                line.startsWith("Shared_Clean:") -> shared += extract(line)
                line.startsWith("Private_Clean:") -> private += extract(line)
            }
        }

        return Smaps(pssKb = pss, rssKb = rss, sharedKb = shared, privateKb = private)
    }

    private fun extract(line: String): Long {
        return Regex("(\\d+)").find(line)?.groupValues?.get(1)?.toLongOrNull() ?: 0L
    }
}
