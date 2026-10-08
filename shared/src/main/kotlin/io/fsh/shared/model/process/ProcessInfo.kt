// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.shared.model.process

import android.os.Parcel
import android.os.Parcelable

data class ProcessInfo(
    val pid: Int,
    val uid: Int,
    val name: String,
    val packageName: String?,
    val rssKb: Long,
    val pssKb: Long,
    val cpuPercent: Float,
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(pid)
        dest.writeInt(uid)
        dest.writeString(name)
        dest.writeString(packageName)
        dest.writeLong(rssKb)
        dest.writeLong(pssKb)
        dest.writeFloat(cpuPercent)
    }

    companion object CREATOR : Parcelable.Creator<ProcessInfo> {
        override fun createFromParcel(parcel: Parcel): ProcessInfo = ProcessInfo(
            pid = parcel.readInt(),
            uid = parcel.readInt(),
            name = parcel.readString() ?: "",
            packageName = parcel.readString(),
            rssKb = parcel.readLong(),
            pssKb = parcel.readLong(),
            cpuPercent = parcel.readFloat(),
        )

        override fun newArray(size: Int): Array<ProcessInfo?> = arrayOfNulls(size)
    }
}
