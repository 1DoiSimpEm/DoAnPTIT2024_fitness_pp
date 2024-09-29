package com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.luminance
import ptit.vietpq.fitnessapp.designsystem.internal.LocalThemeColors
import ptit.vietpq.fitnessapp.designsystem.internal.ThemeColors
import ptit.vietpq.fitnessapp.designsystem.internal.ThemeDimens
import ptit.vietpq.fitnessapp.designsystem.internal.ThemeShapes
import ptit.vietpq.fitnessapp.designsystem.internal.ThemeTypo

object FitnessTheme {
  val color: ThemeColors
    @Composable
    @ReadOnlyComposable
    get() = LocalThemeColors.current

  val typo: ThemeTypo
    @Composable
    @ReadOnlyComposable
    get() = ThemeTypo()

  val shape: ThemeShapes
    @Composable
    @ReadOnlyComposable
    get() = ThemeShapes()

  val dimen: ThemeDimens
    @Composable
    @ReadOnlyComposable
    get() = ThemeDimens()

  val isDark: Boolean
    @Composable
    @ReadOnlyComposable
    get() = color.background.luminance() < 0.5
}
