# Autofill Controls
Whenever possible, supply previously entered data values to text input fields to reduce redundant data entry. This is done in support of the WCAG [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG22/#identify-input-purpose) and [Success Criterion 3.3.7 Redundant Entry](https://www.w3.org/TR/WCAG22/#redundant-entry).

Two approaches to auto-filling input field values are to connect `TextField` data to an Autofill Service and to supply known application data.

## Connect `TextField` data to an Autofill Service

Text input fields can be autofilled by connecting their underlying state data to an `AutofillNode`, and requesting that autofill information be populated when the fields each receive focus. By doing so, any installed Autofill Service can provide suggested data values to those fields. 

* See [Autofill Overview](https://developer.android.com/reference/kotlin/androidx/compose/ui/autofill/package-summary) (especially [AutofillType](https://developer.android.com/reference/kotlin/androidx/compose/ui/autofill/AutofillType)) and the [ExplicitAutofillTypesDemo.kt](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui/integration-tests/ui-demos/src/main/java/androidx/compose/ui/demos/autofill/ExplicitAutofillTypesDemo.kt) sample code for details. 
    * Also, read the View UI framework document [Optimize your app for autofill](https://developer.android.com/guide/topics/text/autofill-optimize) for background on the Android autofill framework and Autofill Services.
* This approach, and supplying [Keyboard Types](../interactions/KeyboardTypes.md), are necessary to fulfill WCAG [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG22/#identify-input-purpose) on Android.

For example:

```kotlin
@Composable
fun AutofilledEmailTextField(
    email: String,
    setEmail: (String) -> Unit
) {
    // Technique: Create an AutofillNode with AutofillType(s) and state setter lambda. Connect it to 
    // the LocalAutofillTree.  
    val autofillNode = AutofillNode(listOf(AutofillType.EmailAddress), onFill = setEmail)
    LocalAutofillTree.current += autofillNode
    
    // Define access to LocalAutofill for later use in a non-composable context.
    val localAutofill = LocalAutofill.current

    OutlinedTextField(
        value = email,
        onValueChange = setEmail,
        modifier = Modifier
            // ... handle keyboard trap, etc. ...
            .onFocusChanged { focusState ->
                // Technique: on focus change, request autofill data or cancel existing request.
                localAutofill?.run {
                    if (focusState.isFocused) {
                        requestAutofillForNode(autofillNode)
                    } else {
                        cancelAutofillForNode(autofillNode)
                    }
                }
            }
            // Technique: Set the autofillNode bounding box for pop-up positioning.
            .onGloballyPositioned { autofillNode.boundingBox = it.boundsInWindow() },
        // Always label TextFields...
        label = { Text("Email")},
        // Use KeyboardType.Email to enter email addresses.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        // ... other TextField properties ...
    )
}
```

## Supply known application data

Input fields can also be auto-populated by supplying default data values from known application data. For example, shipping address fields can be filled from previously-entered billing address data, which will simplify data entry when the billing and shipping addresses are the same.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

© Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.