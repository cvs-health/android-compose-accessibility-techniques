# android-compose-accessibility-techniques

Demonstrates a variety of accessibility best practices for Android Jetpack Compose-based UIs, as well as the problems they can address.
Using the app will demonstrate the impact of accessibility best practices, and reviewing the app project source code will help you learn how to apply those techniques in working code.

See [android-compose-accessibility-techniques Architecture](ARCHITECTURE.md) for details of the app architecture and the project's file structure.

Since some of the code demonstrates the effect of inaccessible coding practices, the app itself does not fully conform to required accessibility guidelines.

## Topics
- Informative Content
    - [x] [Text alternatives](doc/content/TextAlternatives.md)
    - [x] [Accessibility traversal order](doc/content/AccessibilityTraversalOrder.md)
    - [ ] Text resizing and reflow
    - [x] [Content grouping](doc/content/ContentGrouping.md)
    - [x] [Content group replacement](doc/content/ContentGroupReplacement.md)
    - [x] [Heading semantics](doc/content/HeadingSemantics.md)
    - [x] [List semantics](doc/content/ListSemantics.md)
    - [ ] Device orientation and screen sizes
    - [ ] Dark theme
- Interactive Behaviors
    - [x] [Input field labels](doc/interactions/InteractiveControlLabels.md)
    - [ ] Minimum touch target size
    - [ ] Tap target grouping
    - [x] [UX change announcements](doc/interactions/UXChangeAnnouncements.md)
    - [x] [Keyboard types and options](doc/interactions/KeyboardTypes.md)
    - [x] [Keyboard actions](doc/interactions/KeyboardActions.md)
    - [x] [Keyboard focus order](doc/interactions/KeyboardFocusOrder.md)
    - [x] [Custom focus indicators](doc/interactions/CustomFocusIndicators.md)
    - [x] [Custom state descriptions](doc/interactions/CustomStateDescriptions.md)
    - [x] [Custom accessibility actions](doc/interactions/CustomAccessibilityActions.md)
    - [ ] Custom semantics properties
- Specific Component Types
    - [x] [Accordion controls](doc/components/AccordionControls.md)
    - [ ] Autofill input fields
    - [ ] BottomAppBar layouts
    - [ ] Button controls
    - [x] [Checkbox controls](doc/components/CheckboxControls.md)
    - [ ] Chip controls
    - [ ] Dialogs
    - [x] [Dropdown selection menus](doc/components/DropdownMenus.md)
    - [x] [Links inline with text](doc/components/LinksInlineWithText.md)
    - [x] [ListItem layouts](doc/components/ListItemLayouts.md)
    - [x] [ModalBottomSheet layouts](doc/components/ModalBottomSheetLayouts.md)
    - [x] [NavigationBar layouts](doc/components/NavigationBarLayouts.md)
    - [ ] Progress indicators
    - [x] [RadioButton groups](doc/components/RadioButtonGroups.md)
    - [ ] Slider and RangeSlider controls
    - [x] [Switch controls](doc/components/SwitchControls.md)
    - [x] [Tab rows](doc/components/TabRows.md)
    - [x] [TextField controls](doc/components/TextFieldControls.md)
    - [ ] TopAppBar layouts
    
- Other
    - [x] [Compose Semantics automated testing](doc/AutomatedComposeAccessibilityTesting.md)

## License
android-compose-accessibility-techniques is licensed under under the Apache License, Version 2.0.  See [LICENSE](LICENSE) file for more information.

Copyright 2023-2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
