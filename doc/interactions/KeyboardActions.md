# Keyboard Actions
It is generally necessary to explicitly set the soft keyboard actions of text input controls in order to create a good user experience. The appropriate soft (virtual) keyboard action should be based on the text input field's purpose or its position in a form.

The soft keyboard actions of any `TextField` composable are set by its:

1. `Modifier.keyboardOptions` `imeAction` property
2. `Modifier.keyboardActions` property

The `imeAction` property of `Modifier.keyboardOptions` determined the soft keyboard action icon displayed. It can have the values described in [ImeAction](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/ImeAction):

* `ImeAction.Default`
* `ImeAction.Done`
* `ImeAction.Go`
* `ImeAction.Next`
* `ImeAction.None`
* `ImeAction.Previous`
* `ImeAction.Search`
* `ImeAction.Send`

The `Modifier.keyboardActions` property provide callbacks for all keyboard actions available. This data structure is described in [KeyboardActions](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/KeyboardActions):

```kotlin
KeyboardActions(
    onDone: (KeyboardActionScope.() -> Unit)?,
    onGo: (KeyboardActionScope.() -> Unit)?,
    onNext: (KeyboardActionScope.() -> Unit)?,
    onPrevious: (KeyboardActionScope.() -> Unit)?,
    onSearch: (KeyboardActionScope.() -> Unit)?,
    onSend: (KeyboardActionScope.() -> Unit)?
)
```

Examples:

* A search field should use `imeAction=ImeAction.Search` and `keyboardActions = KeyboardActions( onSearch = {...} )`.
* A field at the beginning or in the middle of a form should probably use `imeAction=ImeAction.Next` and `keyboardActions = KeyboardActions( onNext = {...} )`.
* The last field in a form should probably use `imeAction=ImeAction.Done` and `keyboardActions = KeyboardActions( onDone = {...} )`.

For example, a Phone number field can be used to dial a number from the soft (virtual) keyboard with `ImeAction.Go`:

```kotlin
OutlinedTextField(
    // ...
    label = {
        Text("Phone")
    },
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Go
    ),
    keyboardActions = KeyboardActions(
        onGo = { /* TODO */ }
    )
)
```

The `keyboardOptions` property can either be set by copying the `KeyboardOptions.Default` object (with adjusted properties) or by creating a new object using the `KeyboardOptions()` constructor.

Notes:

* Adding `android:windowSoftInputMode="adjustResize"` to the `<activity>` element in AndroidManifest.xml can help fit both screen content and the soft keyboard on-screen at the same time.
* It is also necessary to handle the keyboard trap in default `TextField` composables to avoid issues with WCAG [Success Criterion 2.1.2 No Keyboard Trap](https://www.w3.org/TR/WCAG22/#no-keyboard-trap).
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.

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