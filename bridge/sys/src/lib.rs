// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

pub mod ffi;
pub mod proc;

pub fn version() -> &'static str {
    env!("CARGO_PKG_VERSION")
}
