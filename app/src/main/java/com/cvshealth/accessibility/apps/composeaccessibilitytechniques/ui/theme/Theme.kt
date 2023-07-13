package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/*
COLORS:
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>

    <color name="cvs_red">#FFCC0000</color>
    <color name="cvs_red_dark">#FF930000</color>
    <color name="slate_blue">#FF455A64</color>
    <color name="success_green">#FF118738</color>
    <color name="error_red">#FFDB3321</color>
    <color name="focus_indicator_outline">#FF000000</color>
</resources>
LIGHT COLORS:
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/cvs_red</item>
        <item name="colorPrimaryVariant">@color/cvs_red_dark</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/slate_blue</item>
        <item name="colorSecondaryVariant">@color/slate_blue</item>
        <item name="colorOnSecondary">@color/white</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
DARK COLORS:
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/cvs_red</item>
        <item name="colorPrimaryVariant">#FFDB4D4D</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">#FF98ABB4</item>
        <item name="colorSecondaryVariant">#FF98ABB4</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
 */

private val DarkColorScheme = darkColorScheme(
    primary = CvsRed,
//    primaryContainer = CvsRed,
//    outline = CvsRed,
    secondary = BlueGray,
    onSecondary = Color.Black,
    tertiary = CvsRedDark //Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = CvsRed,
//    primaryContainer = CvsRed,
//    outline = CvsRed,
    secondary = SlateBlue,
    tertiary = SlateBlue//Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposeAccessibilityTechniquesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}