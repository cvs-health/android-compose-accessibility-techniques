# Dark and Light Themes
Supporting both dark and light themes will benefit people with a variety of visual disabilities and all people in different lighting settings. Doing so is also in accordance with the WAI Mobile Task Force guidance about [supporting the characteristic properties of the platform](https://w3c.github.io/Mobile-A11y-TF-Note/#support-the-characteristic-properties-of-the-platform) (in this case, the "Dark theme" setting). 

Material Design Theming is a large topic that is generally outside the scope of this sample application. However, the following steps will demonstrate setting up both Dark and Light themes in an application:

* Use [Material Theme Builder](https://m3.material.io/theme-builder) to define your app's colors.
* Export "Jetpack Compose (Theme.kt)" to download a ZIP archive file containing both dark and light theme palettes and a skeleton app theme composable.
    * Uncompress the downloaded `material-theme.zip` file.
    * Import the generated color definitions to `app/src/main/java/.../ui/theme/Color.kt`.
    * Import the generated theme definition to `app/src/main/java/.../ui/theme/Theme.kt`. 
* Correct the generated `Theme.kt`:
    * Use the appropriate app-specific package name, as Material Theme Builder emits `package com.example.compose`, which will not match this file's actual package location.
    * Use an app-specific theme composable name, as Material Theme Builder uses the generic name `AppTheme`.
* Enhance the generated application theme in `Theme.kt` (as shown in the example code below):
    * Account for Android 13's Dynamic Themes, as Material Theme Builder does not generate that code at this time.
    * Theme the Activity status bar using a `SideEffect`.
* Wrap all composable content in the app-specific theme composable, e.g. in the `setContent` of `MainActivity.kt`.

Note: Be sure both Light theme and Dark theme colors provide sufficient contrast.

See the codelab [Theming in Compose with Material 3](https://developer.android.com/codelabs/jetpack-compose-theming), especially [Section 4. Color schemes](https://developer.android.com/codelabs/jetpack-compose-theming#3), for more details.

## Example

`Color.kt` might contain:

```kotlin
// ...
val md_theme_light_primary = Color(0xFFC00000)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
// ...
val md_theme_dark_primary = Color(0xFFFFB4A8)
val md_theme_dark_onPrimary = Color(0xFF690000)
// ...
```

`Theme.kt` might contain:

```kotlin
package com.mydomain.myapp.ui.theme
// ...
private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    // ...
)
private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    // ...
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+, but allow app to override its use.
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Set the app theme colors to Dark or Light Theme for dynamic or static color schemes.
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    
    // Theme the Activity status bar color.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
  
    // Emit the MaterialTheme that the rest of app will access for theme constants.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // defined in Type.kt
        content = content
    )
}
```

`MainActivity.kt` might contain:

```kotlin
// ...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Technique: Wrap all app content in the app's theme.
            MyAppTheme {
                // Technique: A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
// ...
```

## View framework theme compatibility
If your app also uses the Android View framework in addition to Jetpack Compose, the following techniques are suggested:

* In `res/values/themes.xml`, define an application-wide style based on a Material Design `DayNight` theme. 
    * Verify that this theme name is used in  AndroidManifest.xml's `<application>` and `<activity>` elements' `android:theme` properties.
* Use [Material Theme Builder](https://m3.material.io/theme-builder) to define your app's colors exactly as above. 
    * Export "Android Views (XML)" to download a ZIP archive file containing one XML color resources file and two XML theme resource files (for dark and light themes).
    * Uncompress the downloaded `material-theme.zip` file. (Material Theme Builder always exports to this filename.)
* Import the generated `values/color.xml` definitions to `app/src/main/res/values/color.xml`.
* Import the generated `values/theme.xml` definition to `app/src/main/res/values/theme.xml`.
* Import the generated `values-night/theme.xml` definition to `app/src/main/res/values-night/theme.xml`.
* Add any additional color resources or Material Theme override settings to these files.

----

Copyright 2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.