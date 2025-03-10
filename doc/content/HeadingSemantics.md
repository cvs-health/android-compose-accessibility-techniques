# Heading Semantics
The heading text marking primary content sections of a screen must be marked as an accessibility heading. This supports the WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships), which requires information conveyed through presentation (such as larger-sized heading text) to be programmatically available to accessibility services. This principal extends to the titles of top app bars, dialogs, and bottom sheets.

Mark a composable as an accessibility heading by applying `Modifier.semantics { heading() }`. 

For example:

```
Text("This is a heading",
    style = MaterialTheme.typography.headlineSmall,
    modifier = modifier
        .fillMaxWidth()
        .semantics { heading() }
)
```

Notes:

* Another complication of heading semantics on Android is that list items in the standard lazy list composables (such as `LazyColumn`) can not fully acts as accessibility headings. List items marked with `heading()` should announce as headings, but heading navigation may or may not work in a lazy list. Logically, this makes a certain sense since not all of the list items are initially known.
* Never use `contentDescription` semantics property to append an accessibility property like "Heading" to a composable. It may sound almost correct in TalkBack, but doesn't work correctly.
* Native Android apps do not have multiple heading levels. Therefore, only mark the labels of primary sections as semantic headings; otherwise, the extra headings can make heading navigation in TalkBack long and confusing.
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
