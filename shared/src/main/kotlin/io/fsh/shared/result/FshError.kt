// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.shared.result

sealed class FshError(override val message: String) : Throwable(message) {
    class BackendUnavailable(message: String = "no privileged backend available") : FshError(message)
    class PermissionDenied(message: String = "permission denied") : FshError(message)
    class ParseError(message: String) : FshError(message)
    class IoError(message: String, val cause2: Throwable? = null) : FshError(message)
    class Unknown(message: String) : FshError(message)
}
