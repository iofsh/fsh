// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.logd.parser

import android.os.Build

object VersionProbe {

    fun entryVersion(): Int {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> 4
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> 3
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> 2
            else -> 1
        }
    }

    fun supportsCompression(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
