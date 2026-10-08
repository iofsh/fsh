// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.parser

import java.util.zip.Inflater

object CompressionParser {

    fun decompress(data: ByteArray): ByteArray {
        return try {
            val inflater = Inflater()
            inflater.setInput(data)
            val output = ByteArray(data.size * 4)
            val n = inflater.inflate(output)
            inflater.end()
            output.copyOf(n)
        } catch (_: Throwable) {
            data
        }
    }
}
