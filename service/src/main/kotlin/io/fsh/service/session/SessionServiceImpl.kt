// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.session

import io.fsh.service.aidl.ISessionService
import io.fsh.service.pty.PtyService

class SessionServiceImpl : ISessionService.Stub() {

    private val pty = PtyService()

    override fun createSession(shell: String): Int = pty.create(shell)

    override fun closeSession(sessionId: Int) = pty.close(sessionId)

    override fun write(sessionId: Int, data: ByteArray) = pty.write(sessionId, data)

    override fun read(sessionId: Int): ByteArray = pty.read(sessionId)

    override fun resize(sessionId: Int, cols: Int, rows: Int) = pty.resize(sessionId, cols, rows)
}
