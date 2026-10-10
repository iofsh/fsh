// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

use std::fs;

pub fn read_status(pid: i32) -> Option<String> {
    fs::read_to_string(format!("/proc/{}/status", pid)).ok()
}

pub fn read_stat(pid: i32) -> Option<String> {
    fs::read_to_string(format!("/proc/{}/stat", pid)).ok()
}

pub fn read_cmdline(pid: i32) -> Option<String> {
    fs::read(format!("/proc/{}/cmdline", pid)).ok().map(|b| {
        String::from_utf8_lossy(&b).replace('\0', " ").trim().to_string()
    })
}
