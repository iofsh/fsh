// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.backend.root

object RootBackend {

    fun isAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("su", "-c", "id").redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor() == 0 && output.contains("uid=0")
        } catch (_: Throwable) {
            false
        }
    }

    fun exec(command: String): String {
        return try {
            val process = ProcessBuilder("su", "-c", command)
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
