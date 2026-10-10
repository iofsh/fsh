// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

use crate::proc::stat::Stat;
use crate::proc::status::Status;

pub fn parse_status(raw: &str) -> Option<Status> {
    let mut s = Status::default();
    for line in raw.lines() {
        if let Some(v) = line.strip_prefix("Pid:") {
            s.pid = v.trim().parse().ok()?;
        } else if let Some(v) = line.strip_prefix("PPid:") {
            s.ppid = v.trim().parse().ok()?;
        } else if let Some(v) = line.strip_prefix("Uid:") {
            s.uid = v.split_whitespace().next()?.parse().ok()?;
        } else if let Some(v) = line.strip_prefix("Name:") {
            s.name = v.trim().to_string();
        } else if let Some(v) = line.strip_prefix("VmRSS:") {
            s.rss_kb = v.split_whitespace().next()?.parse().ok()?;
        } else if let Some(v) = line.strip_prefix("Threads:") {
            s.threads = v.trim().parse().ok()?;
        }
    }
    Some(s)
}

pub fn parse_stat(raw: &str) -> Option<Stat> {
    let parts: Vec<&str> = raw.split_whitespace().collect();
    if parts.len() < 15 { return None; }
    Some(Stat {
        pid: parts[0].parse().ok()?,
        utime: parts[13].parse().ok()?,
        stime: parts[14].parse().ok()?,
    })
}
