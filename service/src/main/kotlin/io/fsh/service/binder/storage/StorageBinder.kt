// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.storage

import android.os.Environment
import android.os.StatFs

class StorageBinder {

    fun dataUsage(): StorageSnapshot {
        val stat = StatFs(Environment.getDataDirectory().absolutePath)
        return StorageSnapshot(
            totalBytes = stat.totalBytes,
            freeBytes = stat.availableBytes,
        )
    }

    fun externalUsage(): StorageSnapshot {
        val dir = Environment.getExternalStorageDirectory()
        val stat = StatFs(dir.absolutePath)
        return StorageSnapshot(
            totalBytes = stat.totalBytes,
            freeBytes = stat.availableBytes,
        )
    }
}

data class StorageSnapshot(
    val totalBytes: Long,
    val freeBytes: Long,
)
