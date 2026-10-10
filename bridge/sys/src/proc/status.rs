// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

#[derive(Debug, Clone, Default)]
pub struct Status {
    pub pid: i32,
    pub uid: i32,
    pub ppid: i32,
    pub name: String,
    pub rss_kb: u64,
    pub threads: i32,
}
