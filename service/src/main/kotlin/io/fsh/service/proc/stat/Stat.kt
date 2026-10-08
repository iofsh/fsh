// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.stat

data class Stat(
    val pid: Int,
    val utime: Long,
    val stime: Long,
)
