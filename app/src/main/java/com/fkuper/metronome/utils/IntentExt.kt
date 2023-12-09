package com.fkuper.metronome.utils

import android.content.Intent
import android.os.Parcelable

fun Intent.putParcelableExtra(key: String, value: Parcelable) {
    putExtra(key, value)
}