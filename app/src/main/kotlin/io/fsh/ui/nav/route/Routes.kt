// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.graphics.vector.ImageVector

enum class Routes(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    SUMMARY(
        route = "summary",
        label = "Summary",
        icon = Icons.Default.Home,
    ),
    PERFORMANCE(
        route = "performance",
        label = "Performance",
        icon = Icons.Default.Assessment,
    ),
    PROCESSES(
        route = "processes",
        label = "Processes",
        icon = Icons.Default.List,
    ),
    LOGS(
        route = "logs",
        label = "Logs",
        icon = Icons.Default.Info,
    ),
    TERMINAL(
        route = "terminal",
        label = "Terminal",
        icon = Icons.Default.Terminal,
    ),
    SERVICES(
        route = "services",
        label = "Services",
        icon = Icons.Default.Build,
    ),
    PACKAGES(
        route = "packages",
        label = "Packages",
        icon = Icons.Default.Memory,
    ),
    SETTINGS(
        route = "settings",
        label = "Settings",
        icon = Icons.Default.Settings,
    ),
}
