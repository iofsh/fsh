// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.backend

import java.io.File

object BackendSelector {

    fun current(): Backend {
        if (isRootAvailable()) return Backend.ROOT
        if (isShizukuAvailable()) return Backend.SHIZUKU
        if (isAdbAvailable()) return Backend.ADB
        return Backend.NONE
    }

    private fun isRootAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("su", "-c", "id").redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor() == 0 && output.contains("uid=0")
        } catch (_: Throwable) {
            false
        }
    }

    private fun isShizukuAvailable(): Boolean {
        return try {
            Class.forName("rikka.shizuku.Shizuku")
            rikka.shizuku.Shizuku.pingBinder()
        } catch (_: Throwable) {
            false
        }
    }

    private fun isAdbAvailable(): Boolean {
        return File("/data/local/tmp").canRead()
    }
}
