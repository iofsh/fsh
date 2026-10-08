// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.pty.stream

import kotlin.concurrent.thread

class PtyStream(
    val id: Int,
    private val ptyMaster: String,
    private val shell: String,
) {

    private var process: Process? = null
    private var running = false
    private var worker: Thread? = null
    private val buffer = ArrayDeque<Byte>()

    fun start() {
        running = true
        process = ProcessBuilder(shell).redirectErrorStream(true).start()
        worker = thread(name = "fsh-pty-$id") {
            val input = process?.inputStream ?: return@thread
            val buf = ByteArray(4096)
            while (running) {
                val n = try { input.read(buf) } catch (_: Throwable) { -1 }
                if (n <= 0) break
                synchronized(buffer) {
                    for (i in 0 until n) buffer.addLast(buf[i])
                }
            }
        }
    }

    fun stop() {
        running = false
        worker?.interrupt()
        process?.destroy()
        process = null
    }

    fun write(data: ByteArray) {
        try {
            process?.outputStream?.write(data)
            process?.outputStream?.flush()
        } catch (_: Throwable) {
        }
    }

    fun read(): ByteArray {
        synchronized(buffer) {
            if (buffer.isEmpty()) return ByteArray(0)
            val out = ByteArray(buffer.size)
            var i = 0
            while (buffer.isNotEmpty()) {
                out[i++] = buffer.removeFirst()
            }
            return out
        }
    }

    fun resize(cols: Int, rows: Int) {
    }
}
