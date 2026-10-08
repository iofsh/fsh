// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.client

import java.nio.ByteBuffer
import java.nio.ByteOrder

object LogdHandshake {

    const val FLAG_NONBLOCK = 0x1
    const val FLAG_TAIL = 0x2
    const val FLAG_CLEAR = 0x4

    fun build(logId: Int, tail: Int, flags: Int): ByteArray {
        val buffer = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(logId)
        buffer.putInt(tail)
        buffer.putInt(flags)
        return buffer.array()
    }
}
