// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.status

data class Status(
    val pid: Int,
    val uid: Int,
    val ppid: Int,
    val name: String,
    val rssKb: Long,
    val threads: Int,
)
