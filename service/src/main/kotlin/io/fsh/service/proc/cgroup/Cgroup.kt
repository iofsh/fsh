// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.proc.cgroup

data class Cgroup(
    val controller: String,
    val path: String,
)
