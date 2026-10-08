// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service;

import io.fsh.service.IProcessService;
import io.fsh.service.ILogService;
import io.fsh.service.ISessionService;

interface IPrivilegedService {
    IProcessService getProcessService();
    ILogService getLogService();
    ISessionService getSessionService();
    int getVersion();
}