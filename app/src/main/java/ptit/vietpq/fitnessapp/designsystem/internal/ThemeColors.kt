package ptit.vietpq.fitnessapp.designsystem.internal

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme

abstract class ThemeColors {
  abstract val background: Color
  abstract val surface: Color
  abstract val surfaceVariant: Color
  abstract val onSurfacePrimary: Color
  abstract val onSurfaceSecondary: Color
  abstract val onSurfaceTertiary: Color
  abstract val primaryIndicator: Color
  abstract val serviceBackgroundWithGroups: Color
  abstract val switchTrack: Color
  abstract val switchThumb: Color
  val primary: Color = Color(0xFF896CFE)
  val limeGreen: Color = Color(0xFFE2F163)
  val primaryDisable = Color(0xFFC5EED5)
  val primaryDark: Color = Color(0xFFD81F26)

  val button: Color = primary
  val onButton: Color = Color.White

  val divider: Color
    get() = surfaceVariant

  val iconTint: Color
    get() = onSurfaceSecondary

  val error: Color = Color(0xFFB9171E)
  val black: Color = Color(0xFF232323)
  val semiBlack: Color = Color(0xFF373737)
  val neutral1: Color = Color(0xFF161616)
}

@Composable
fun isDarkTheme() = FitnessTheme.color.background.luminance() < 0.5

val LocalThemeColors = staticCompositionLocalOf<ThemeColors> {
  ThemeColorsLight()
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme.Light }

enum class AppTheme {
  Auto,
  Light,
  Dark,
}

@Composable
fun QRCodeScannerScanBarcodeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  val isInDarkTheme = when (LocalAppTheme.current) {
    AppTheme.Auto -> isSystemInDarkTheme()
    AppTheme.Light -> false
    AppTheme.Dark -> true
  }

  val colors: ThemeColors = when (isInDarkTheme) {
    true -> ThemeColorsDark()
    false -> ThemeColorsLight()
  }

  val window = (LocalView.current.context as Activity).window
  val insetsController = WindowCompat.getInsetsController(window, window.decorView)

  insetsController.apply {
    hide(WindowInsetsCompat.Type.navigationBars())
    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
  }

  val colorScheme: ColorScheme = when (isInDarkTheme) {
    true -> darkColorScheme(
      primary = colors.primary,
      onPrimary = colors.onSurfacePrimary,
      background = colors.background,
      onBackground = colors.onSurfacePrimary,
      surface = colors.surface,
      onSurface = colors.onSurfacePrimary,
      surfaceVariant = colors.surface,
      onSurfaceVariant = colors.onSurfacePrimary,
    )

    else -> lightColorScheme(
      primary = colors.primary,
      onPrimary = colors.onSurfacePrimary,
      background = colors.background,
      onBackground = colors.onSurfacePrimary,
      surface = colors.surface,
      onSurface = colors.onSurfacePrimary,
      surfaceVariant = colors.surface,
      onSurfaceVariant = colors.onSurfacePrimary,
    )
  }

  CompositionLocalProvider(
    LocalThemeColors provides colors,
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      content = content,
    )
  }
}
