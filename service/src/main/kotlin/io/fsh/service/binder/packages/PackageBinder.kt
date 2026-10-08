// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.binder.packages

import android.content.Context

class PackageBinder(private val context: Context) {

    fun packagesForUid(uid: Int): List<String> {
        return try {
            context.packageManager.getPackagesForUid(uid)?.toList() ?: emptyList()
        } catch (_: Throwable) {
            emptyList()
        }
    }

    fun packageNameForUid(uid: Int): String? {
        return packagesForUid(uid).firstOrNull()
    }
}
