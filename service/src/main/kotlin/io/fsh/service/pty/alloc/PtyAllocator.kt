// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.pty.alloc

import java.io.File

class PtyAllocator {

    fun allocate(): String {
        val candidates = listOf("/dev/ptmx", "/dev/pts/ptmx")
        for (candidate in candidates) {
            if (File(candidate).exists()) return candidate
        }
        error("no pty master available")
    }
}
