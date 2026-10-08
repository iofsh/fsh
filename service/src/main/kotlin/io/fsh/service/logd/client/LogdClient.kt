// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.client

import io.fsh.shared.model.log.LogEntry
import io.fsh.service.logd.parser.EntryParser
import io.fsh.service.logd.socket.LogdrSocket
import kotlin.concurrent.thread

class LogdClient(
    private val logId: Int,
    private val minPriority: Int,
    private val onEntry: (LogEntry) -> Unit,
) {

    private val parser = EntryParser()
    private var running = false
    private var worker: Thread? = null

    fun start() {
        running = true
        worker = thread(name = "fsh-logd") {
            runLoop()
        }
    }

    fun stop() {
        running = false
        worker?.interrupt()
        worker = null
    }

    private fun runLoop() {
        try {
            val socket = LogdrSocket()
            socket.open(logId, tail = -1, flags = 0x1)
            val input = socket.inputStream()

            while (running) {
                val entry = parser.readEntry(input) ?: break
                if (entry.priority >= minPriority) {
                    onEntry(entry)
                }
            }
            socket.close()
        } catch (_: Throwable) {
        }
    }
}
