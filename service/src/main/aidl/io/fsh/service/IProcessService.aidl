// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service;

import io.fsh.shared.model.process.ProcessInfo;

interface IProcessService {
    List<ProcessInfo> getProcesses();
    ProcessInfo getProcess(int pid);
    boolean killProcess(int pid);
    boolean forceStop(String packageName, int userId);
    long getTotalMemory();
    long getAvailableMemory();
}
