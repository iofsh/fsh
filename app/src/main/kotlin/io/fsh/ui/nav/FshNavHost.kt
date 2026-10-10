// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
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

private val RAIL_WIDTH = 88.dp
private val WIDE_THRESHOLD = 600.dp

@Composable
fun FshNavHost() {
    val nav = rememberNavController()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val wide = maxWidth >= WIDE_THRESHOLD

        if (wide) {
            Row(modifier = Modifier.fillMaxSize()) {
                FshSidebar(nav = nav)
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    FshGraph(nav = nav, onMenu = {})
                }
            }
        } else {
            val drawer = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawer,
                drawerContent = {
                    ModalDrawerSheet {
                        FshDrawerHeader()
                        HorizontalDivider()
                        Spacer(Modifier.height(8.dp))
                        FshDrawerItems(nav = nav) {
                            scope.launch { drawer.close() }
                        }
                    }
                },
            ) {
                FshGraph(
                    nav = nav,
                    onMenu = { scope.launch { drawer.open() } },
                )
            }
        }
    }
}

@Composable
private fun FshSidebar(nav: NavHostController) {
    Surface(
        modifier = Modifier
            .width(RAIL_WIDTH)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Fsh",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            Routes.entries.forEach { route ->
                FshSidebarItem(
                    route = route,
                    selected = nav.currentRoute() == route.route,
                    onClick = {
                        nav.navigate(route.route) {
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun FshSidebarItem(
    route: Routes,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = route.icon,
            contentDescription = route.label,
            tint = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = route.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FshDrawerHeader() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
        Text(
            text = "Fsh",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "the last thing a process sees",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FshDrawerItems(
    nav: NavHostController,
    onNavigate: () -> Unit,
) {
    Routes.entries.forEach { route ->
        NavigationDrawerItem(
            label = { Text(route.label) },
            icon = { Icon(route.icon, contentDescription = null) },
            selected = nav.currentRoute() == route.route,
            onClick = {
                nav.navigate(route.route) {
                    popUpTo(nav.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
                onNavigate()
            },
            colors = NavigationDrawerItemDefaults.colors(),
            modifier = Modifier.padding(horizontal = 12.dp),
        )
    }
}

@Composable
private fun FshGraph(
    nav: NavHostController,
    onMenu: () -> Unit,
) {
    NavHost(
        navController = nav,
        startDestination = Routes.SUMMARY.route,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(Routes.SUMMARY.route) { SummaryScreen(onMenu = onMenu) }
        composable(Routes.PERFORMANCE.route) { PerformanceScreen(onMenu = onMenu) }
        composable(Routes.PROCESSES.route) { ProcessesScreen(onMenu = onMenu) }
        composable(Routes.LOGS.route) { LogsScreen(onMenu = onMenu) }
        composable(Routes.TERMINAL.route) { TerminalScreen(onMenu = onMenu) }
        composable(Routes.SERVICES.route) { ServicesScreen(onMenu = onMenu) }
        composable(Routes.PACKAGES.route) { PackagesScreen(onMenu = onMenu) }
        composable(Routes.SETTINGS.route) { SettingsScreen(onMenu = onMenu) }
    }
}

@Composable
private fun NavHostController.currentRoute(): String? =
    currentBackStackEntryAsState().value?.destination?.route
