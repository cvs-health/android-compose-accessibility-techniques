# Automated Compose Accessibility Testing
Automated Compose jUnit UI tests automatically test the application's accessibility, because all such tests operate only on the app's semantics tree, not its displayed UI. That said, it is appropriate to focus on verifying specific accessibility semantics when constructing tests, as well as verifying app functionality.

See [testing your Compose layout](https://developer.android.com/jetpack/compose/testing) for Compose jUnit test setup details.

Useful semantic accessibility tests include:

* Verify that all intended headings are accessibility headings (and that non-heading text are not headings)

For example, given this Composable, the test code following will verify that it has appropriate heading semantics.

```kotlin
@Composable
fun SampleScreen() {
    Text(
        text = stringResource(id = R.string.sample_screen_heading),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .testTag("screenHeading")
            .fillMaxWidth()
            .semantics { heading() }
    )
    Text(
        text = stringResource(R.string.sample_screen_description),
        modifier = Modifier
            .testTag("bodyCopy")
            .fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium
    )
}
```

```kotlin
class SampleTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            AppTheme {
                SampleScreen()
            }
        }
    }

    @Test
    fun verifyScreenHeadingIsSemanticHeading() {
        composeTestRule
            .onNode(hasTestTag("screenHeading") and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag("bodyCopy") and !isHeading())
            .assertExists()
    }
}
```

* Verify contentDescription on informative and actionable Images (and null contentDescription on decorative images)

```kotlin
@Test
fun verifyThatInformativeImageHasContentDescription() {
    composeTestRule
        .onNode(hasTestTag("testAvatarImage") and hasContentDescription("John Smith's avatar"))
        .assertExists()
}

@Test
fun verifyThatDecorativeImageHasNoContentDescription() {
    // Note the empty varargs list in hasContentDescriptionExactly() -- this will verify that no contentDescription is present
    composeTestRule
        .onNode(hasTestTag("decorativeImage") and hasContentDescriptionExactly())
        .assertExists()
}
```

To verify actual application texts, use `createAndroidComposeRule<MainActivity>()`, and retrieve string values with `testRule.activity.getString(R.string.sample_screen_heading)`.

* Verify that toggleable layouts are toggleable and do toggle:

For example:

```kotlin
@Test
fun verifyThatCheckboxRowIsToggleable() {
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .assertIsToggleable()
}

@Test
fun verifyThatCheckboxRowToggles() {
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .assertIsOff()
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .performScrollTo() // <-- Note this performScrollTo(); it's important to scroll to a composable before acting on it.
        .performClick()
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .assertIsOn()
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .performClick() // Here performScrollTo() isn't needed, because we did it above and screen location wasn't changed.
    composeTestRule
        .onNodeWithTag("checkboxRow")
        .assertIsOff()
}
```

Other tests for accessibility semantics are possible and are strongly suggested. The key technique is to test for semantic properties, as well as testing for functionality.


----

Copyright 2023 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
