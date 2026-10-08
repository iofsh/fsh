// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.pty

import io.fsh.service.pty.alloc.PtyAllocator
import io.fsh.service.pty.stream.PtyStream

class PtyService {

    private val allocator = PtyAllocator()
    private val streams = mutableMapOf<Int, PtyStream>()
    private var nextId = 1

    @Synchronized
    fun create(shell: String): Int {
        val id = nextId++
        val pty = allocator.allocate()
        val stream = PtyStream(id, pty, shell)
        stream.start()
        streams[id] = stream
        return id
    }

    @Synchronized
    fun close(id: Int) {
        streams.remove(id)?.stop()
    }

    @Synchronized
    fun write(id: Int, data: ByteArray) {
        streams[id]?.write(data)
    }

    @Synchronized
    fun read(id: Int): ByteArray {
        return streams[id]?.read() ?: ByteArray(0)
    }

    @Synchronized
    fun resize(id: Int, cols: Int, rows: Int) {
        streams[id]?.resize(cols, rows)
    }
}
