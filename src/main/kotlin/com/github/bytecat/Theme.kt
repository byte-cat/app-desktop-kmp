package com.github.bytecat

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun themeColors(): Colors {
    return if (isSystemInDarkTheme()) {
        darkColors(background = Color.Black)
    } else {
        lightColors(background = Color.White)
    }
}

private val darkTheme = Theme(
    myCatBackgroundColor = 0x202020,
    myCatBorderColor = 0xE0E0E0,
    myCatNameColor = 0xE0E0E0,
    itemBackgroundColor = 0x202020,
    itemIconTintColor = 0xE0E0E0,
    itemBorderColor = 0xE0E0E0,
    dividerColor = 0x303030
)

private val lightTheme = Theme(
    myCatBackgroundColor = 0xF0F0F0,
    myCatBorderColor = 0x343434,
    myCatNameColor = 0x343434,
    itemBackgroundColor = 0xF0F0F0,
    itemIconTintColor = 0x343434,
    itemBorderColor = 0x343434,
    dividerColor = 0xD0D0D0
)

@Composable
fun myCatTheme(): Theme {
    return if (isSystemInDarkTheme()) {
        darkTheme
    } else {
        lightTheme
    }
}

class Theme(
    myCatBackgroundColor: Long,
    myCatBorderColor: Long,
    myCatNameColor: Long,
    itemBackgroundColor: Long,
    itemIconTintColor: Long,
    itemBorderColor: Long,
    dividerColor: Long
) {
    val myCatBackgroundColor = Color(0xFF000000 or myCatBackgroundColor)
    val myCatBorderColor = Color(0xFF000000 or myCatBorderColor)
    val myCatNameColor = Color(0xFF000000 or myCatNameColor)

    val itemBackgroundColor = Color(0xFF000000 or itemBackgroundColor)

    val itemIconTintColor = Color(0xFF000000 or itemIconTintColor)
    val itemBorderColor = Color(0xFF000000 or itemBorderColor)
    val dividerColor = Color(0xFF000000 or dividerColor)

}