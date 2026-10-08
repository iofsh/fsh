// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.metrics.net

import android.content.Context
import android.net.TrafficStats

class NetCollector(private val context: Context) {

    private var lastRx = 0L
    private var lastTx = 0L
    private var lastTime = 0L

    fun collect(): NetSnapshot {
        val rx = TrafficStats.getTotalRxBytes()
        val tx = TrafficStats.getTotalTxBytes()
        val now = System.currentTimeMillis()

        val deltaRx = if (lastTime > 0) rx - lastRx else 0L
        val deltaTx = if (lastTime > 0) tx - lastTx else 0L
        val deltaMs = if (lastTime > 0) now - lastTime else 1L

        lastRx = rx
        lastTx = tx
        lastTime = now

        return NetSnapshot(
            rxBytes = rx,
            txBytes = tx,
            rxRate = if (deltaMs > 0) deltaRx * 1000L / deltaMs else 0L,
            txRate = if (deltaMs > 0) deltaTx * 1000L / deltaMs else 0L,
        )
    }
}

data class NetSnapshot(
    val rxBytes: Long,
    val txBytes: Long,
    val rxRate: Long,
    val txRate: Long,
)
