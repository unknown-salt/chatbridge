package com.chatbridge.utils

fun String.toColor(): Int = Integer.decode(this)

fun Int.toHexColor(): String = String.format("#%06X", this)

