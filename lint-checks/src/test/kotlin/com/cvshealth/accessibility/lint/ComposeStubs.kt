/*
 * Copyright 2026 CVS Health and/or one of its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.accessibility.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFile

/**
 * Mock stubs for the Compose and Material 3 APIs used by sample code in these tests.
 *
 * These are required, not optional. The lint test harness compiles each sample snippet in a
 * JVM-only environment with no Android or Compose APIs on the classpath. Without stubs the
 * snippet does not compile, UAST never produces a usable parse tree, `visitMethodCall` is never
 * invoked, and the detector reports nothing — so an `expectClean()` assertion passes for a
 * reason that has nothing to do with the rule being correct. Any test that calls
 * `allowCompilationErrors()` is asserting against an empty parse tree.
 *
 * Modelled on `ComposeStubs.kt` in the `lint-rules` module of
 * `cvs-health-source-code/android-compose-accessibility-techniques` (branch `cvs_testbed`),
 * extended to cover the wider API surface this module's rules touch.
 *
 * Stubs declare each symbol in its real package, because the sample code imports real package
 * paths and UAST has to resolve them.
 */
object ComposeStubs {

    val Runtime: TestFile = kotlin(
        """
        package androidx.compose.runtime

        @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
        annotation class Composable

        interface State<out T> { val value: T }

        class MutableState<T>(override var value: T) : State<T>

        fun <T> mutableStateOf(value: T): MutableState<T> = MutableState(value)

        fun <T> remember(calculation: () -> T): T = calculation()
        """.trimIndent()
    )

    val Ui: TestFile = kotlin(
        """
        package androidx.compose.ui

        interface Modifier {
            companion object : Modifier
        }
        """.trimIndent()
    )

    val Unit_: TestFile = kotlin(
        """
        package androidx.compose.ui.unit

        class Dp(val value: Float)
        val Int.dp: Dp get() = Dp(this.toFloat())

        class TextUnit(val value: Float)
        val Int.sp: TextUnit get() = TextUnit(this.toFloat())
        val Double.sp: TextUnit get() = TextUnit(this.toFloat())
        """.trimIndent()
    )

    val Graphics: TestFile = kotlin(
        """
        package androidx.compose.ui.graphics

        class Color(val value: Long) {
            companion object {
                val Black = Color(0xFF000000)
                val White = Color(0xFFFFFFFF)
                val Red = Color(0xFFFF0000)
                val Gray = Color(0xFF888888)
                val DarkGray = Color(0xFF444444)
                val LightGray = Color(0xFFCCCCCC)
                val Transparent = Color(0x00000000)
            }
        }
        """.trimIndent()
    )

    val Text_: TestFile = kotlin(
        """
        package androidx.compose.ui.text

        import androidx.compose.ui.unit.TextUnit

        class TextStyle(val fontSize: TextUnit? = null)
        """.trimIndent()
    )

    val TextInput: TestFile = kotlin(
        """
        package androidx.compose.ui.text.input

        value class KeyboardType private constructor(private val value: Int) {
            companion object {
                val Text = KeyboardType(0)
                val Ascii = KeyboardType(1)
                val Number = KeyboardType(2)
                val Phone = KeyboardType(3)
                val Uri = KeyboardType(4)
                val Email = KeyboardType(5)
                val Password = KeyboardType(6)
                val NumberPassword = KeyboardType(7)
                val Decimal = KeyboardType(8)
            }
        }
        """.trimIndent()
    )

    val FoundationText: TestFile = kotlin(
        """
        package androidx.compose.foundation.text

        import androidx.compose.ui.text.input.KeyboardType

        class KeyboardOptions(
            val keyboardType: KeyboardType = KeyboardType.Text,
            val autoCorrect: Boolean = true
        )
        """.trimIndent()
    )

    // Requires Ui, Graphics
    val Semantics: TestFile = kotlin(
        """
        package androidx.compose.ui.semantics

        import androidx.compose.ui.Modifier

        value class Role private constructor(private val value: Int) {
            companion object {
                val Button = Role(0)
                val Checkbox = Role(1)
                val Switch = Role(2)
                val RadioButton = Role(3)
                val Tab = Role(4)
                val Image = Role(5)
                val DropdownList = Role(6)
            }
        }

        value class LiveRegionMode private constructor(private val value: Int) {
            companion object {
                val Polite = LiveRegionMode(0)
                val Assertive = LiveRegionMode(1)
            }
        }

        value class ContentType private constructor(private val value: Int) {
            companion object {
                val EmailAddress = ContentType(0)
                val Username = ContentType(1)
                val Password = ContentType(2)
                val PhoneNumber = ContentType(3)
                val PostalCode = ContentType(4)
                val BirthDateFull = ContentType(5)
            }
        }

        interface SemanticsPropertyReceiver {
            var contentDescription: String
            var stateDescription: String
            var paneTitle: String
            var role: Role
            var liveRegion: LiveRegionMode
            var contentType: ContentType
            var autofill: Boolean
            var text: String
            var testTag: String
            var disabled: Boolean
            fun heading()
            fun invisibleToUser()
        }

        fun Modifier.semantics(
            mergeDescendants: Boolean = false,
            properties: SemanticsPropertyReceiver.() -> Unit
        ): Modifier = this

        fun Modifier.clearAndSetSemantics(
            properties: SemanticsPropertyReceiver.() -> Unit
        ): Modifier = this
        """.trimIndent()
    )

    // Requires Ui, Graphics, Runtime
    val Foundation: TestFile = kotlin(
        """
        package androidx.compose.foundation

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color

        fun Modifier.clickable(
            enabled: Boolean = true,
            onClickLabel: String? = null,
            onClick: () -> Unit
        ): Modifier = this

        fun Modifier.background(color: Color): Modifier = this

        @Composable
        fun Image(
            painter: Any? = null,
            contentDescription: String?,
            modifier: Modifier = Modifier
        ) {
        }
        """.trimIndent()
    )

    // Requires Ui
    val FoundationSelection: TestFile = kotlin(
        """
        package androidx.compose.foundation.selection

        import androidx.compose.ui.Modifier
        import androidx.compose.ui.semantics.Role
        import androidx.compose.ui.state.ToggleableState

        fun Modifier.selectableGroup(): Modifier = this

        fun Modifier.selectable(
            selected: Boolean,
            enabled: Boolean = true,
            role: Role? = null,
            onClick: () -> Unit
        ): Modifier = this

        fun Modifier.toggleable(
            value: Boolean,
            enabled: Boolean = true,
            role: Role? = null,
            onValueChange: (Boolean) -> Unit
        ): Modifier = this

        fun Modifier.triStateToggleable(
            state: ToggleableState,
            enabled: Boolean = true,
            role: Role? = null,
            onClick: () -> Unit
        ): Modifier = this
        """.trimIndent()
    )

    val UiState: TestFile = kotlin(
        """
        package androidx.compose.ui.state

        enum class ToggleableState { On, Off, Indeterminate }
        """.trimIndent()
    )

    // Requires Ui, Runtime
    val FoundationLayout: TestFile = kotlin(
        """
        package androidx.compose.foundation.layout

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier

        interface ColumnScope
        interface RowScope
        interface BoxScope {
            fun Modifier.align(alignment: Any): Modifier
        }

        @Composable
        fun Column(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
        }

        @Composable
        fun Row(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
        }

        @Composable
        fun Box(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
        }
        """.trimIndent()
    )

    // Requires Ui, Runtime. Lazy containers matter because they are the real-world shape that
    // defeats a rule looking for a Column or Row parent: the children sit in an `items { }`
    // lambda inside a LazyRow, not directly in the container call.
    val FoundationLazy: TestFile = kotlin(
        """
        package androidx.compose.foundation.lazy

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier

        interface LazyItemScope

        interface LazyListScope {
            fun item(content: @Composable LazyItemScope.() -> Unit)
            fun <T> items(items: List<T>, itemContent: @Composable LazyItemScope.(T) -> Unit)
            fun items(count: Int, itemContent: @Composable LazyItemScope.(Int) -> Unit)
        }

        @Composable
        fun LazyRow(modifier: Modifier = Modifier, content: LazyListScope.() -> Unit) {
        }

        @Composable
        fun LazyColumn(modifier: Modifier = Modifier, content: LazyListScope.() -> Unit) {
        }
        """.trimIndent()
    )

    val UiDraw: TestFile = kotlin(
        """
        package androidx.compose.ui.draw

        import androidx.compose.ui.Modifier

        fun Modifier.alpha(alpha: Float): Modifier = this
        """.trimIndent()
    )

    val UiPointer: TestFile = kotlin(
        """
        package androidx.compose.ui.input.pointer

        import androidx.compose.ui.Modifier

        interface PointerInputScope

        fun Modifier.pointerInput(
            key1: Any?,
            block: suspend PointerInputScope.() -> Unit
        ): Modifier = this

        suspend fun PointerInputScope.detectTapGestures(
            onLongPress: ((Any) -> Unit)? = null,
            onTap: ((Any) -> Unit)? = null
        ) {
        }
        """.trimIndent()
    )

    val UiFocus: TestFile = kotlin(
        """
        package androidx.compose.ui.focus

        import androidx.compose.ui.Modifier

        class FocusRequester {
            fun requestFocus() {}
        }

        fun Modifier.focusRequester(focusRequester: FocusRequester): Modifier = this
        """.trimIndent()
    )

    val UiWindow: TestFile = kotlin(
        """
        package androidx.compose.ui.window

        import androidx.compose.runtime.Composable

        @Composable
        fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
        }
        """.trimIndent()
    )

    val Animation: TestFile = kotlin(
        """
        package androidx.compose.animation

        import androidx.compose.runtime.Composable

        @Composable
        fun AnimatedVisibility(visible: Boolean, content: @Composable () -> Unit) {
        }
        """.trimIndent()
    )

    val AnimationCore: TestFile = kotlin(
        """
        package androidx.compose.animation.core

        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.State

        @Composable
        fun animateFloatAsState(targetValue: Float): State<Float> =
            object : State<Float> { override val value: Float = targetValue }
        """.trimIndent()
    )

    // Requires Ui, Runtime, Graphics, FoundationText, Text_
    val Material3: TestFile = kotlin(
        """
        package androidx.compose.material3

        import androidx.compose.runtime.Composable
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.text.TextStyle
        import androidx.compose.ui.unit.TextUnit
        import androidx.compose.foundation.text.KeyboardOptions

        class ColorScheme(
            val primary: Color = Color.Black,
            val onPrimary: Color = Color.White,
            val background: Color = Color.White,
            val onBackground: Color = Color.Black,
            val surface: Color = Color.White,
            val onSurface: Color = Color.Black,
            val error: Color = Color.Red
        )

        class Typography(
            val displayLarge: TextStyle = TextStyle(),
            val displayMedium: TextStyle = TextStyle(),
            val displaySmall: TextStyle = TextStyle(),
            val headlineLarge: TextStyle = TextStyle(),
            val headlineMedium: TextStyle = TextStyle(),
            val headlineSmall: TextStyle = TextStyle(),
            val titleLarge: TextStyle = TextStyle(),
            val titleMedium: TextStyle = TextStyle(),
            val titleSmall: TextStyle = TextStyle(),
            val bodyLarge: TextStyle = TextStyle(),
            val bodyMedium: TextStyle = TextStyle(),
            val bodySmall: TextStyle = TextStyle(),
            val labelLarge: TextStyle = TextStyle(),
            val labelMedium: TextStyle = TextStyle(),
            val labelSmall: TextStyle = TextStyle()
        )

        object MaterialTheme {
            val colorScheme: ColorScheme = ColorScheme()
            val typography: Typography = Typography()
        }

        @Composable
        fun Text(
            text: String,
            modifier: Modifier = Modifier,
            color: Color? = null,
            fontSize: TextUnit? = null,
            style: TextStyle? = null,
            maxLines: Int = Int.MAX_VALUE
        ) {
        }

        @Composable
        fun Icon(
            imageVector: Any? = null,
            contentDescription: String?,
            modifier: Modifier = Modifier,
            tint: Color? = null
        ) {
        }

        @Composable
        fun Button(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            content: @Composable () -> Unit
        ) {
        }

        @Composable
        fun TextButton(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            content: @Composable () -> Unit
        ) {
        }

        @Composable
        fun IconButton(
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            content: @Composable () -> Unit
        ) {
        }

        @Composable
        fun TextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier = Modifier,
            label: (@Composable () -> Unit)? = null,
            placeholder: (@Composable () -> Unit)? = null,
            keyboardOptions: KeyboardOptions = KeyboardOptions(),
            isError: Boolean = false,
            supportingText: (@Composable () -> Unit)? = null
        ) {
        }

        @Composable
        fun OutlinedTextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier = Modifier,
            label: (@Composable () -> Unit)? = null,
            placeholder: (@Composable () -> Unit)? = null,
            keyboardOptions: KeyboardOptions = KeyboardOptions(),
            isError: Boolean = false,
            supportingText: (@Composable () -> Unit)? = null
        ) {
        }

        @Composable
        fun RadioButton(
            selected: Boolean,
            onClick: (() -> Unit)?,
            modifier: Modifier = Modifier,
            enabled: Boolean = true
        ) {
        }

        @Composable
        fun Checkbox(
            checked: Boolean,
            onCheckedChange: ((Boolean) -> Unit)?,
            modifier: Modifier = Modifier,
            enabled: Boolean = true
        ) {
        }

        @Composable
        fun Switch(
            checked: Boolean,
            onCheckedChange: ((Boolean) -> Unit)?,
            modifier: Modifier = Modifier,
            enabled: Boolean = true
        ) {
        }

        @Composable
        fun Slider(
            value: Float,
            onValueChange: (Float) -> Unit,
            modifier: Modifier = Modifier,
            enabled: Boolean = true
        ) {
        }

        @Composable
        fun RangeSlider(
            value: ClosedFloatingPointRange<Float>,
            onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
            modifier: Modifier = Modifier,
            enabled: Boolean = true
        ) {
        }

        @Composable
        fun Tab(
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier = Modifier,
            text: (@Composable () -> Unit)? = null,
            icon: (@Composable () -> Unit)? = null
        ) {
        }

        @Composable
        fun LeadingIconTab(
            selected: Boolean,
            onClick: () -> Unit,
            text: @Composable () -> Unit,
            icon: @Composable () -> Unit,
            modifier: Modifier = Modifier
        ) {
        }

        @Composable
        fun TopAppBar(
            title: @Composable () -> Unit,
            modifier: Modifier = Modifier,
            navigationIcon: @Composable () -> Unit = {},
            actions: @Composable () -> Unit = {}
        ) {
        }

        @Composable
        fun Scaffold(
            modifier: Modifier = Modifier,
            topBar: @Composable () -> Unit = {},
            bottomBar: @Composable () -> Unit = {},
            content: @Composable (Any) -> Unit
        ) {
        }

        @Composable
        fun AlertDialog(
            onDismissRequest: () -> Unit,
            confirmButton: @Composable () -> Unit,
            modifier: Modifier = Modifier,
            dismissButton: (@Composable () -> Unit)? = null,
            title: (@Composable () -> Unit)? = null,
            text: (@Composable () -> Unit)? = null
        ) {
        }

        interface ExposedDropdownMenuBoxScope

        @Composable
        fun ExposedDropdownMenuBox(
            expanded: Boolean,
            onExpandedChange: (Boolean) -> Unit,
            modifier: Modifier = Modifier,
            content: @Composable ExposedDropdownMenuBoxScope.() -> Unit
        ) {
        }

        @Composable
        fun NavigationBarItem(
            selected: Boolean,
            onClick: () -> Unit,
            icon: @Composable () -> Unit,
            modifier: Modifier = Modifier,
            label: (@Composable () -> Unit)? = null
        ) {
        }
        """.trimIndent()
    )

    /**
     * Every stub, in dependency order. Pass with a spread operator ahead of the sample under test:
     * `lint().files(*ComposeStubs.all, kotlin("""...""")).run()`.
     */
    val all: Array<TestFile>
        get() = arrayOf(
            Runtime,
            Ui,
            Unit_,
            Graphics,
            Text_,
            TextInput,
            FoundationText,
            UiState,
            Semantics,
            Foundation,
            FoundationSelection,
            FoundationLayout,
            FoundationLazy,
            UiDraw,
            UiPointer,
            UiFocus,
            UiWindow,
            Animation,
            AnimationCore,
            Material3,
        )
}
