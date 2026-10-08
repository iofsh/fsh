// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd

import io.fsh.service.aidl.ILogService
import io.fsh.service.aidl.LogEntry
import io.fsh.service.logd.buffer.RingBuffer
import io.fsh.service.logd.client.LogdClient

class LogServiceImpl : ILogService.Stub() {

    private val buffer = RingBuffer(1000)
    private var client: LogdClient? = null

    @Synchronized
    override fun startStream(logId: Int, minPriority: Int) {
        stopStream()
        val logdClient = LogdClient(
            logId = logId,
            minPriority = minPriority,
            onEntry = { entry -> buffer.add(entry) }
        )
        logdClient.start()
        client = logdClient
    }

    @Synchronized
    override fun stopStream() {
        client?.stop()
        client = null
    }

    @Synchronized
    override fun getRecent(count: Int): List<LogEntry> = buffer.takeLast(count)

    @Synchronized
    override fun clear() {
        buffer.clear()
    }
}
