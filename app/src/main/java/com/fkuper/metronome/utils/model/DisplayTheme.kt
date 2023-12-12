package com.fkuper.metronome.utils.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.ui.graphics.vector.ImageVector

enum class DisplayTheme(val icon: ImageVector, val text: String) {
    AUTO(Icons.Rounded.AutoAwesome, "Auto"),
    DARK(Icons.Rounded.DarkMode, "Dark"),
    LIGHT(Icons.Rounded.LightMode, "Light")
}