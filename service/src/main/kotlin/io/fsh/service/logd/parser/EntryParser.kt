// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.parser

import io.fsh.shared.model.log.LogEntry
import java.io.EOFException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class EntryParser {

    private val payloadParser = PayloadParser()

    fun readEntry(input: InputStream): LogEntry? {
        return try {
            val header = ByteArray(20)
            var read = 0
            while (read < 20) {
                val n = input.read(header, read, 20 - read)
                if (n < 0) return null
                read += n
            }

            val buffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN)
            val len = buffer.short.toInt() and 0xFFFF
            val second = buffer.short.toInt() and 0xFFFF

            val pid: Int
            val tid: Int
            val sec: Int
            val nsec: Int
            var uid = -1

            if (second == 0) {
                pid = buffer.int
                tid = buffer.int
                sec = buffer.int
                nsec = buffer.int
            } else {
                val headerSize = second
                pid = buffer.int
                tid = buffer.int
                sec = buffer.int
                nsec = buffer.int
                val lid = buffer.int
                if (headerSize >= 28) {
                    uid = buffer.int
                }
            }

            val payloadLen = len - 20 + 4
            if (payloadLen <= 0) return null

            val payload = ByteArray(payloadLen)
            var pRead = 0
            while (pRead < payloadLen) {
                val n = input.read(payload, pRead, payloadLen - pRead)
                if (n < 0) return null
                pRead += n
            }

            payloadParser.parse(payload, pid, tid, sec, nsec, uid)
        } catch (_: EOFException) {
            null
        } catch (_: Throwable) {
            null
        }
    }
}
