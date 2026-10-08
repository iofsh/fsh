// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.net

data class NetUnix(
    val path: String,
    val state: String,
    val uid: Int,
)
