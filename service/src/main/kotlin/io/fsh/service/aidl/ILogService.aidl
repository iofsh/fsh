// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.aidl;

import io.fsh.service.aidl.LogEntry;

interface ILogService {
    void startStream(int logId, int minPriority);
    void stopStream();
    List<LogEntry> getRecent(int count);
    void clear();
}
