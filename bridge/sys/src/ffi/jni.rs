// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

use jni::objects::JClass;
use jni::sys::jstring;
use jni::JNIEnv;

#[no_mangle]
pub extern "system" fn Java_io_fsh_bridge_NativeBridge_version(
    mut env: JNIEnv,
    _class: JClass,
) -> jstring {
    let s = crate::version();
    env.new_string(s).unwrap().into_raw()
}

#[no_mangle]
pub extern "system" fn Java_io_fsh_bridge_NativeBridge_readStatus(
    mut env: JNIEnv,
    _class: JClass,
    pid: jni::sys::jint,
) -> jstring {
    let out = crate::proc::reader::read_status(pid as i32).unwrap_or_default();
    env.new_string(out).unwrap().into_raw()
}
