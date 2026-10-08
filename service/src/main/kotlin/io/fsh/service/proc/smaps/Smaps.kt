// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.smaps

data class Smaps(
    val pssKb: Long,
    val rssKb: Long,
    val sharedKb: Long,
    val privateKb: Long,
)
