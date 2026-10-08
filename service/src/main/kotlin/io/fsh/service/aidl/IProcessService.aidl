// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.aidl;

import io.fsh.service.aidl.ProcessInfo;

interface IProcessService {
    List<ProcessInfo> getProcesses();
    ProcessInfo getProcess(int pid);
    boolean killProcess(int pid);
    boolean forceStop(String packageName, int userId);
    long getTotalMemory();
    long getAvailableMemory();
}
