// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.fsh.service.aidl.ILogService
import io.fsh.service.aidl.IPrivilegedService
import io.fsh.service.aidl.IProcessService
import io.fsh.service.aidl.ISessionService
import io.fsh.service.logd.LogServiceImpl
import io.fsh.service.proc.ProcessServiceImpl
import io.fsh.service.session.SessionServiceImpl

class PrivilegedService : Service() {

    private val processService by lazy { ProcessServiceImpl() }
    private val logService by lazy { LogServiceImpl() }
    private val sessionService by lazy { SessionServiceImpl() }

    private val binder = object : IPrivilegedService.Stub() {
        override fun getProcessService(): IProcessService = processService
        override fun getLogService(): ILogService = logService
        override fun getSessionService(): ISessionService = sessionService
        override fun getVersion(): Int = ServiceEntry.VERSION
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        logService.stopStream()
        super.onDestroy()
    }
}
