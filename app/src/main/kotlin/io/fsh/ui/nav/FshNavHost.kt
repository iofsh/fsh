// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.fsh.ui.screen.logs.LogsScreen
import io.fsh.ui.screen.packages.PackagesScreen
import io.fsh.ui.screen.performance.PerformanceScreen
import io.fsh.ui.screen.processes.ProcessesScreen
import io.fsh.ui.screen.services.ServicesScreen
import io.fsh.ui.screen.settings.SettingsScreen
import io.fsh.ui.screen.summary.SummaryScreen
import io.fsh.ui.screen.terminal.TerminalScreen
import kotlinx.coroutines.launch

@Composable
fun FshNavHost() {
    val nav = rememberNavController()
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val openDrawer: () -> Unit = { scope.launch { drawer.open() } }

    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            FshDrawer(
                nav = nav,
                onItemClick = { route ->
                    nav.navigate(route) {
                        popUpTo(nav.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawer.close() }
                },
            )
        },
    ) {
        NavHost(
            navController = nav,
            startDestination = Routes.SUMMARY.route,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(Routes.SUMMARY.route) {
                SummaryScreen(onMenu = openDrawer)
            }
            composable(Routes.PERFORMANCE.route) {
                PerformanceScreen(onMenu = openDrawer)
            }
            composable(Routes.PROCESSES.route) {
                ProcessesScreen(onMenu = openDrawer)
            }
            composable(Routes.LOGS.route) {
                LogsScreen(onMenu = openDrawer)
            }
            composable(Routes.TERMINAL.route) {
                TerminalScreen(onMenu = openDrawer)
            }
            composable(Routes.SERVICES.route) {
                ServicesScreen(onMenu = openDrawer)
            }
            composable(Routes.PACKAGES.route) {
                PackagesScreen(onMenu = openDrawer)
            }
            composable(Routes.SETTINGS.route) {
                SettingsScreen(onMenu = openDrawer)
            }
        }
    }
}

@Composable
private fun FshDrawer(
    nav: NavHostController,
    onItemClick: (String) -> Unit,
) {
    val current by nav.currentBackStackEntryAsState()
    val currentRoute = current?.destination?.route

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(vertical = 24.dp)) {
            Text(
                text = "Fsh",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Text(
                text = "the last thing a process sees",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
        }

        HorizontalDivider()

        Spacer(Modifier.height(8.dp))

        Routes.entries.forEach { route ->
            NavigationDrawerItem(
                label = { Text(route.label) },
                icon = { Icon(route.icon, contentDescription = null) },
                selected = currentRoute == route.route,
                onClick = { onItemClick(route.route) },
                colors = NavigationDrawerItemDefaults.colors(),
                modifier = Modifier.padding(horizontal = 12.dp),
            )
        }
    }
}
