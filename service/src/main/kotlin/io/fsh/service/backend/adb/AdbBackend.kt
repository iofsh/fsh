// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.backend.adb

import java.io.File

object AdbBackend {

    fun isAvailable(): Boolean = File("/data/local/tmp").canRead()

    fun exec(command: String): String {
        return try {
            val process = ProcessBuilder("sh", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (_: Throwable) {
            ""
        }
    }
}
