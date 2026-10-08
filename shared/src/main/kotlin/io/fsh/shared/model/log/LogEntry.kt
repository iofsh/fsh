// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.service.aidl

import android.os.Parcel
import android.os.Parcelable

data class LogEntry(
    val timestamp: Long,
    val pid: Int,
    val tid: Int,
    val priority: Int,
    val tag: String,
    val message: String,
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(timestamp)
        dest.writeInt(pid)
        dest.writeInt(tid)
        dest.writeInt(priority)
        dest.writeString(tag)
        dest.writeString(message)
    }

    companion object CREATOR : Parcelable.Creator<LogEntry> {
        override fun createFromParcel(parcel: Parcel): LogEntry = LogEntry(
            timestamp = parcel.readLong(),
            pid = parcel.readInt(),
            tid = parcel.readInt(),
            priority = parcel.readInt(),
            tag = parcel.readString() ?: "",
            message = parcel.readString() ?: "",
        )

        override fun newArray(size: Int): Array<LogEntry?> = arrayOfNulls(size)
    }
}
