// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright (C) 2026 Fsh contributors

package io.fsh.shared.result

sealed interface Result<out T> {
    data class Ok<T>(val value: T) : Result<T>
    data class Err(val error: FshError) : Result<Nothing>
}

inline fun <T, R> Result<T>.map(block: (T) -> R): Result<R> = when (this) {
    is Result.Ok -> Result.Ok(block(value))
    is Result.Err -> this
}

inline fun <T> Result<T>.onOk(block: (T) -> Unit): Result<T> {
    if (this is Result.Ok) block(value)
    return this
}

inline fun <T> Result<T>.onErr(block: (FshError) -> Unit): Result<T> {
    if (this is Result.Err) block(error)
    return this
}

fun <T> Result<T>.getOrNull(): T? = (this as? Result.Ok)?.value
