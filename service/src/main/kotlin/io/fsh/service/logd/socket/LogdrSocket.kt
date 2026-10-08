// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.socket

import android.net.LocalSocket
import android.net.LocalSocketAddress
import io.fsh.service.logd.client.LogdHandshake
import java.io.InputStream
import java.io.OutputStream

class LogdrSocket {

    private var socket: LocalSocket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    fun open(logId: Int, tail: Int, flags: Int) {
        val s = LocalSocket(LocalSocket.SOCKET_SEQPACKET)
        s.connect(LocalSocketAddress("logdr", LocalSocketAddress.Namespace.RESERVED))
        socket = s
        input = s.inputStream
        output = s.outputStream

        val handshake = LogdHandshake.build(logId, tail, flags)
        output?.write(handshake)
        output?.flush()
    }

    fun inputStream(): InputStream = input ?: error("socket not open")

    fun close() {
        try {
            input?.close()
            output?.close()
            socket?.close()
        } catch (_: Throwable) {
        }
        socket = null
        input = null
        output = null
    }
}
