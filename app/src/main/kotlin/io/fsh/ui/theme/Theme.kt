// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkScheme = darkColorScheme(
    primary = FinBlue,
    onPrimary = Snow,
    primaryContainer = FinDeep,
    onPrimaryContainer = Snow,
    secondary = FinGlow,
    background = Ink,
    onBackground = Bone,
    surface = Paper,
    onSurface = Bone,
    surfaceVariant = Smoke,
    onSurfaceVariant = Mist,
    outline = Smoke,
    error = Danger,
    onError = Snow
)

private val LightScheme = lightColorScheme(
    primary = FinDeep,
    onPrimary = Snow,
    background = Snow,
    onBackground = Ink,
    surface = Bone,
    onSurface = Ink
)

@Composable
fun FshTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = FshTypography,
        shapes = FshShapes,
        content = content
    )
}
