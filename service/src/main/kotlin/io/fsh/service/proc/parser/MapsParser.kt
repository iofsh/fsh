// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.parser

import io.fsh.service.proc.maps.Maps

class MapsParser {

    fun parse(raw: String): List<Maps> {
        val result = mutableListOf<Maps>()
        for (line in raw.lines()) {
            val parts = line.split("\\s+".toRegex())
            if (parts.size < 5) continue
            val range = parts[0]
            val perms = parts[1]
            val name = if (parts.size >= 6) parts.last() else ""
            result += Maps(
                range = range,
                permissions = perms,
                name = name,
            )
        }
        return result
    }
}
