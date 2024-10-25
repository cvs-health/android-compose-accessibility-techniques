# Automated Compose Accessibility Testing
Automated Compose jUnit UI tests automatically test the application's accessibility, because all such tests operate only on the app's semantics tree, not its displayed UI. That said, it is appropriate to focus on verifying specific accessibility semantics when constructing tests, as well as verifying app functionality.

## Compose jUnit UI Testing Setup

See [testing your Compose layout](https://developer.android.com/jetpack/compose/testing) for Compose jUnit test setup details.

In outline, the process is:

* Set up jUnit4 library dependencies in build.gradle.
* Construct jUnit UI tests in `app/src/androidTest` package subfolders.
* In tests, use `createComposeRule` to access semantics tree values. (Or use `createAndroidComposeRule<YourActivity>` if you also need to access `Activity` resources.)

## Sample semantic tests

Many semantic accessibility tests are possible. Some useful examples follow.

### Testing Semantic Headings

Verify that all intended headings are semantic accessibility headings (and that non-heading text are not headings).

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
    }

    @Test
    fun verifyBodyCopyIsNotSemanticHeading() {
        composeTestRule
            .onNode(hasTestTag("bodyCopy") and !isHeading())
            .assertExists()
    }
}
```

### Testing Image Text Alternatives

Verify the contentDescription values on informative and actionable Images (and that purely decorative images have a null contentDescription).

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

Note: See the section [Accessing Activity Resources](#accessing-activity-resources) below for guidance on how to retrieve externalized text string values from an app resource file.

### Testing `toggleable` Layouts

Verify that `toggleable` layouts are toggleable and do toggle.

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

### Testing semantic node counts

To verify that a specific number of nodes exist with a given set of semantic attributes, apply the `composeTestRule.onAllNodes()` method and `.assertCountEquals()`.

For example, in the semantic headings test code above, verifying that there is only one semantic heading would be done as follows:

```kotlin
@Test
fun verifySemanticHeadingCount() {
    composeTestRule.onAllNodes(isHeading()).assertCountEquals(1)
}
```

## Additional Semantic Matchers

A list of available `SemanticMatcher` instances is available in the Compose [Testing cheatsheet](https://developer.android.com/develop/ui/compose/testing/testing-cheatsheet), as well as in the Compose open source code file `Filters.kt`.

In some cases, no convenient `SemanticMatcher` is supplied by Compose; however, these can be added using helper functions.

* To test for the presence or absence of a specific semantic property, use `SemanticsMatcher.keyIsDefined()` or `SemanticsMatcher.keyNotDefined()` with the specific `SemanticsProperties` key.

For example:

```kotlin
fun hasLiveRegion(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.LiveRegion)

fun hasNoLiveRegion(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion)
```

* To test the value of a specific semantic property, retrieve its value using the `getOrNull()` method on a `SemanticsNode.config` property and the specific `SemanticsProperties` key. A `SemanticsMatcher`'s `matcher` lambda provides a `SemanticsNode` that can be used.

For example:

```kotlin
fun hasLiveRegionMode(mode: LiveRegionMode) : SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.LiveRegion == ${mode}") { semanticsNode ->
        mode == semanticsNode.config.getOrNull(SemanticsProperties.LiveRegion)
    }
}
```

* In some cases, the retrieved semantic property is an object with properties of its own that need further calls to access.

For example, to validate the expected number of rows in a collection, use:

```kotlin
fun hasCollectionRowCount(expectedRowCount: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.rowCount == ${expectedRowCount}") {
        expectedRowCount == it.config.getOrNull(SemanticsProperties.CollectionInfo)?.rowCount
    }
}
```

For more helpful SemanticMatcher examples, see [TestHelpers.kt](../app/src/androidTest/java/com/cvshealth/composeaccessibilitytechniques/TestHelpers.kt). 

For more complete lists of available semantic attributes to test, see [SemanticsNode](https://developer.android.com/reference/kotlin/androidx/compose/ui/semantics/SemanticsNode), [SemanticsConfiguration](https://developer.android.com/reference/kotlin/androidx/compose/ui/semantics/SemanticsConfiguration), and the Android source code for `SemanticsProperties`, and `SemanticsActions`.

## Accessing Activity Resources

To write a test that accesses app resource files, such as externalized string values, declare the test rule using `createAndroidComposeRule<SomeActivity>()` (instead of `createComposeRule()`) and then access resources via `testRule.activity` methods.

For example, retrieve string values with `testRule.activity.getString(R.string.sample_screen_heading)`.

Reimplementing the test for `contentDescription` above using external string resources would look something like this:

```kotlin
class SampleAndroidTest {
    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<AvatarActivity>()

    @Test
    fun verifyThatInformativeImageHasContentDescription() {
        androidComposeTestRule
            .onNode(
                hasTestTag("testAvatarImage") 
                and 
                hasContentDescription(
                    androidComposeTestRule.activity.getString(R.string.test_avatar_description)
                )
            )
            .assertExists()
    }
}
```

Note: For a single Activity app, the sole `Activity` must be the target of `createAndroidComposeRule`, and the test class's `setup()` method must navigate to the appropriate composables to test. See [AccordionControlsTests.kt](../app/src/androidTest/java/com/cvshealth/composeaccessibilitytechniques/AccordionControlsTests.kt) for an example.

## Adjusting time in tests
By default, Compose jUnit UI tests automatically advance time to the next frame after performing an action. That is generally all that is necessary, but to test transient effects, manually advancing the test time can be helpful.

To do that, use `composeTestRule.mainClock.advanceTimeBy()`. 

For example:

```kotlin
// Launch a limited-duration Snackbar 
composeTestRule
    .onNodeWithTag(snackbarButtonTestTag)
    .performScrollTo()
    .performClick()

// Verify that the Snackbar is displayed
composeTestRule.onNodeWithText(snackbarText).assertIsDisplayed()

// Move time forward
composeTestRule.mainClock.advanceTimeBy(20000L)

// Verify that Snackbar has dismissed itself.
composeTestRule.onNodeWithText(snackbarText).assertDoesNotExist()
```

(See [PopupMessageTests.kt](../app/src/androidTest/java/com/cvshealth/composeaccessibilitytechniques/PopupMessagesTests.kt) for more information about testing `Snackbar` semantics.)

## Debugging tests
To debug tests, apply the following code in a test function to log the semantics tree: `composeTestRule.onRoot().printToLog("LOG_TAG")`. Alternatively, apply the following code to log the unmerged semantics tree: `composeTestRule.onRoot(useUnmergedTree = true).printToLog("LOG_TAG")`.

If that code fails because of multiple root nodes (such as when pop-up dialogs appear), apply something like this instead: `composeTestRule.onAllNodes(isRoot()).onLast().printToLog("LOG_TAG")`

## Compose Testing Summary
Other tests for accessibility semantics are possible and are strongly suggested. The key technique is to test for expected semantic property values, as well as testing for functionality.

----

Copyright 2023-2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
