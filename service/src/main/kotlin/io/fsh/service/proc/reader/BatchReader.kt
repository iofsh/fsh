// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.reader

class BatchReader(private val reader: ProcReader = ProcReader()) {

    fun readAll(pids: List<Int>): Map<Int, String> {
        val result = mutableMapOf<Int, String>()
        for (pid in pids) {
            val status = reader.readStatus(pid) ?: continue
            result[pid] = status
        }
        return result
    }
}
