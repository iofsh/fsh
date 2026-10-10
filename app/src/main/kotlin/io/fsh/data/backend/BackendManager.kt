// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.data.backend

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import io.fsh.service.IPrivilegedService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import rikka.shizuku.Shizuku
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendManager @Inject constructor(
    private val context: Context,
) {
    private val _state = MutableStateFlow<BackendState>(BackendState.Unknown)
    val state: StateFlow<BackendState> = _state.asStateFlow()

    private var service: IPrivilegedService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = IPrivilegedService.Stub.asInterface(binder)
            _state.value = BackendState.Connected
            Log.i(TAG, "PrivilegedService connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            _state.value = BackendState.Disconnected
            Log.w(TAG, "PrivilegedService disconnected")
        }
    }

    fun service(): IPrivilegedService? = service

    fun detect() {
        when {
            !Shizuku.pingBinder() -> _state.value = BackendState.ShizukuNotRunning
            Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED ->
                _state.value = BackendState.PermissionRequired
            else -> bind()
        }
    }

    fun requestPermission(requestCode: Int) {
        if (Shizuku.isPreV11() ||
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        ) {
            bind()
        } else {
            Shizuku.requestPermission(requestCode)
        }
    }

    fun bind() {
        try {
            val args = Shizuku.UserServiceArgs(
                ComponentName(context.packageName, "io.fsh.service.PrivilegedService")
            )
                .daemon(false)
                .processNameSuffix("shell")
                .debuggable(false)
                .version(1)
            Shizuku.bindUserService(args, connection)
            _state.value = BackendState.Binding
        } catch (t: Throwable) {
            Log.e(TAG, "bind failed", t)
            _state.value = BackendState.Error(t.message ?: "bind failed")
        }
    }

    fun unbind() {
        try {
            val args = Shizuku.UserServiceArgs(
                ComponentName(context.packageName, "io.fsh.service.PrivilegedService")
            )
            Shizuku.unbindUserService(args, connection, true)
        } catch (_: Throwable) {
        }
        service = null
    }

    companion object {
        private const val TAG = "BackendManager"
    }
}

sealed interface BackendState {
    data object Unknown : BackendState
    data object ShizukuNotRunning : BackendState
    data object PermissionRequired : BackendState
    data object Binding : BackendState
    data object Connected : BackendState
    data object Disconnected : BackendState
    data class Error(val message: String) : BackendState
}
