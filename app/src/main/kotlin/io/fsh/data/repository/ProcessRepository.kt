// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.data.repository

import io.fsh.data.backend.BackendManager
import io.fsh.shared.model.process.ProcessInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessRepository @Inject constructor(
    private val backend: BackendManager,
) {
    fun processes(): Flow<List<ProcessInfo>> = flow {
        val svc = backend.service()
        if (svc == null) {
            emit(emptyList())
            return@flow
        }
        emit(svc.processService.processes)
    }.flowOn(Dispatchers.IO)
}
