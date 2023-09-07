# Accordion Controls
Accordion controls cause sections of a screen to expand, revealing additional content, or to collapse, concealing that content. To make accordion controls conform to the WCAG 2 [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG21/#name-role-value), it is necessary for such controls to be marked with the `Modifier.semantics` property `expand()` (if collapsed) and `collapse()` (if expanded). Only by declaring those semantics properties is the expanded or collapsed state of an accordion control surfaced to the Android Accessibility API.

Mark a composable as an accordion heading by applying: 
```kotlin
modifier = Modifier.semantics {
    if (isExpanded) {
        collapse {
            setIsExpanded(false)
            return@collapse true
        }
    } else {
        expand {
            setIsExpanded(true)
            return@expand true
        }
    }
}
```

Also, accordion controls are generally `clickable()` and can improve their user experience by setting the appropriate onClickLabel:

```kotlin
modifier = Modifier.clickable(
    onClickLabel = if (isExpanded)
        stringResource("Collapse")
    else
        stringResource("Expand")
) {
    setIsExpanded(!isExpanded)
}
```

For example:
```
val (isExpanded, setIsExpanded) = rememberSaveable { mutableStateOf(false) }
Column {
     Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = if (isExpanded)
                    stringResource("Collapse")
                else
                    stringResource("Expand")
            ) {
                setIsExpanded(!isExpanded)
            }
            .semantics (mergeDescendants = true) {
                if (isExpanded) {
                    collapse {
                        setIsExpanded(false)
                        return@collapse true
                    }
                } else {
                    expand {
                        setIsExpanded(true)
                        return@expand true
                    }
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("This is an accordion control",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        Icon(
            painter = painterResource(
                id = if (isExpanded)
                    R.drawable.ic_caret_up // a up-caret icon: ^
                else
                    R.drawable.ic_caret_right, // a right-caret icon: >
            ),
            contentDescription = null, // expand/collapsed is a state of the Row
            modifier = Modifier.minimumInteractiveComponentSize()
        )
    }
    if (isExpanded) {
        Text("This is a long expanded section text ...")
        Text("This is more expanded section text ...")
        Text("This is even more expanded section text ...")
    }
}
```

Notes:

* Never use `contentDescription` or `stateDescription` to described an accordion's state. It may sound almost correct in TalkBack, but doesn't work correctly.

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