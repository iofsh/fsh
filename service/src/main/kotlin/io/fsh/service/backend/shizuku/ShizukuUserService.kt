// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.backend.shizuku

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import io.fsh.service.aidl.IPrivilegedService
import rikka.shizuku.Shizuku

class ShizukuUserService {

    private var binder: IPrivilegedService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = IPrivilegedService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }
    }

    fun bind(userId: Int = 0): Boolean {
        return try {
            val args = Shizuku.UserServiceArgs(
                ComponentName("io.fsh", "io.fsh.service.PrivilegedService")
            )
                .daemon(false)
                .processNameSuffix("shell")
                .debuggable(false)
                .version(1)
            Shizuku.bindUserService(args, connection)
            true
        } catch (_: Throwable) {
            false
        }
    }

    fun unbind() {
        try {
            Shizuku.unbindUserService(
                Shizuku.UserServiceArgs(
                    ComponentName("io.fsh", "io.fsh.service.PrivilegedService")
                ),
                connection,
                true
            )
        } catch (_: Throwable) {
        }
    }

    fun service(): IPrivilegedService? = binder
}
