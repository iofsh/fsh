// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.aidl;

interface ISessionService {
    int createSession(String shell);
    void closeSession(int sessionId);
    void write(int sessionId, in byte[] data);
    byte[] read(int sessionId);
    void resize(int sessionId, int cols, int rows);
}
