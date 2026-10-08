// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.net

data class NetTcp(
    val localAddress: String,
    val remoteAddress: String,
    val state: String,
    val uid: Int,
)
