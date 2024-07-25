# Content Grouping
Group content elements that should be read together as a single unit by screen readers. Incorrectly grouped content can be misleading and violate the WCAG [Success Criterion 1.3.2 Meaningful Sequence](https://www.w3.org/TR/WCAG22/#meaningful-sequence). And correctly grouping content can significantly improve the audio-textual user experience.

Two factors must be weighed in the successful design of grouped content:

* How long is the combined text?
* How coherent is the text as a block? (In other words, does it all belong together?)

There is a balance to be drawn here between overly long text, which works poorly in a screen reader, and overly short related pieces of text, which will read choppily in a screen reader and can't be skipped over as a unit to get to the next logical grouping.

Cards and list items are good examples of content that should generally be grouped together and read as a single unit: they are often relatively concise and highly coherent. A screen reader user would also want to skip to the next card or list item with a single swipe, instead of swiping once for each Text in the card or list item.

Another important use for content grouping is associating pieces information which belongs together, but are presented in different `Text` elements. Associating the headers and values in a simple data table is one example.

The key techniques for content grouping are:

## Use an enclosing layout composable with `Modifier.semantics(mergeDescendants = true) {}`

To group content, enclose all the relevant content elements in a single layout composable and set `Modifier.semantics(mergeDescendants = true) {}` on that wrapping composable. This semantic property makes the composable focusable by a screen reader and tells the screen reader to combine all child composable text into a single announcement.

```kotlin
// All of this card's content will be grouped for accessibility.
Card(
    modifier = Modifier.semantics(mergeDescendants = true) {}
) {
    Column {
        Text("Random Article Title", style = MaterialTheme.typography.headlineSmall)
        Text("Author: Some Random Name")
        text("Date: 1 Jan 2022") 
        text("This card for a fake article contains random test text designed to illustrate content grouping. Grouped content should be concise and belong together as a unit.")
    }
}    
```

## Group content labels with their values

Group any separate content labels with their associated text values. For example, group simple table headings together with their table values.

* This is an important point: misgrouping or a lack of grouping could read all the table headings, then the table values, without saying what those table values mean. (Which would violate the WCAG [Success Criterion 1.3.2 Meaningful Sequence](https://www.w3.org/TR/WCAG22/#meaningful-sequence) and [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships).)

* Note that associating active controls and their labels requires different techniques. (See [Interactive Control Labels](../interactions/InteractiveControlLabels.md) for details.)

* Complex data tables also require different handling. (See [Text Alternatives](../content/TextAlternatives.md) for an overview.)

```kotlin
/* This simple table correctly groups "City" with "Boston" and "Population (in 2020)" with 
   "675,647" for accessibility services by using layout structure and 
   Modifier.semantics(mergeDescendants = true) {}. */
Row {
    Column(
        modifier = Modifier.semantics(mergeDescendants = true) {}
    ) {
        Text("City")
        Text("Boston")
    }
    Column(
        modifier = Modifier.semantics(mergeDescendants = true) {}
    ) {
        Text("Population (in 2020)")
        Text("675,647")
    }
}
```

## Disable redundant content announcements

Disable announcement of any redundant content within the grouping layout composable by using `invisibleToUser()` semantics or a null `contentDescription` (as described in [Text Alternatives](../content/TextAlternatives.md)).

Alternatively, override the `contentDescription` of the entire group composable; see [Content Group Replacement](./ContentGroupReplacement.md) for details.

## Apply any `onClick` handling to the appropriate content group

For clickable grouped content (such as cards or list items that allow drilling down to a detailed view), apply an `onClick` handler or a `Modifier.clickable()` property to the appropriate layout group composable. Doing so will set `Modifier.semantics(mergeDescendants = true) {}` implicitly.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

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
