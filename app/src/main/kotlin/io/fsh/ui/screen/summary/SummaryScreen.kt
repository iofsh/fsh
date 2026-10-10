// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.screen.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.fsh.ui.component.card.MetricCard
import io.fsh.ui.theme.FinBlue
import io.fsh.ui.theme.Success
import io.fsh.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    onMenu: () -> Unit,
    vm: SummaryViewModel = hiltViewModel(),
) {
    val stats by vm.stats.collectAsState()
    val backend by vm.backendState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Summary") },
                navigationIcon = {
                    IconButton(onClick = onMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "backend: ${backend::class.simpleName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            MetricCard(
                title = "CPU",
                value = "%.1f%%".format(stats.cpuPercent),
                subtitle = "${stats.processCount} processes · ${stats.threadCount} threads",
                accent = FinBlue,
            )

            MetricCard(
                title = "Memory",
                value = "${stats.memUsed.gb()} / ${stats.memTotal.gb()} GB",
                subtitle = "${(stats.memUsed * 100 / stats.memTotal.coerceAtLeast(1))}% used",
                accent = FinBlue,
            )

            MetricCard(
                title = "Battery",
                value = "${stats.batteryPercent}%",
                subtitle = "%.1f °C".format(stats.batteryTemp),
                accent = if (stats.batteryPercent < 20) Warning else Success,
            )

            MetricCard(
                title = "Storage",
                value = "${stats.storageUsed.gb()} / ${stats.storageTotal.gb()} GB",
                subtitle = "${(stats.storageUsed * 100 / stats.storageTotal.coerceAtLeast(1))}% used",
                accent = FinBlue,
            )

            MetricCard(
                title = "Uptime",
                value = stats.uptimeMs.duration(),
                accent = FinBlue,
            )
        }
    }
}

private fun Long.gb(): String =
    "%.1f".format(this / 1024.0 / 1024.0 / 1024.0)

private fun Long.duration(): String {
    val s = this / 1000
    val h = s / 3600
    val m = (s % 3600) / 60
    return "${h}h ${m}m"
}
