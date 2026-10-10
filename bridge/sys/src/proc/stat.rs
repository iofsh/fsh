// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

#[derive(Debug, Clone, Default)]
pub struct Stat {
    pub pid: i32,
    pub utime: u64,
    pub stime: u64,
}
