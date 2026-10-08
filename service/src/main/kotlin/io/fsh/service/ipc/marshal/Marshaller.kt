// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.ipc.marshal

import java.nio.ByteBuffer
import java.nio.ByteOrder

object Marshaller {

    fun packProcess(
        pid: Int,
        uid: Int,
        rssKb: Long,
        pssKb: Long,
        cpuPercent: Float,
    ): ByteArray {
        val buffer = ByteBuffer.allocate(4 + 4 + 8 + 8 + 4).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(pid)
        buffer.putInt(uid)
        buffer.putLong(rssKb)
        buffer.putLong(pssKb)
        buffer.putFloat(cpuPercent)
        return buffer.array()
    }
}
