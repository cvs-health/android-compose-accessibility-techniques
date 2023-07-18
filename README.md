# android-compose-accessibility-techniques

Demonstrates a variety of accessibility best practices for Android Jetpack Compose-based UIs, as well as the problems they can address.
Using the app will demonstrate the impact of accessibility best practices, and reviewing the app project source code will help you learn how to apply those techniques in working code.

See [android-compose-accessibility-techniques Architecture](ARCHITECTURE.md) for details of the app architecture and the project's file structure.

Since some of the code demonstrates the effect of inaccessible coding practices, the app itself does not fully conform to required accessibility guidelines.

## Topics
- Informative Content
    - [ ] Text alternatives
    - [ ] Text resizing and reflow
    - [ ] Content grouping
    - [ ] Content group replacement
    - [x] [Heading semantics](doc/content/HeadingSemantics.md)
    - [ ] List semantics
    - [ ] Device orientation and screen sizes
    - [ ] Dark theme
- Interactive Controls
    - [x] [Input field labels](doc/controls/InteractiveControlLabels.md)
    - [ ] Minimum touch target size
    - [ ] Tap target grouping
    - [ ] UX change announcements
    - [ ] Keyboard types
    - [ ] Keyboard actions
    - [ ] Custom state descriptions
    - [ ] Custom accessibility actions
    - [ ] Custom semantics properties
- Specific Component Types
    - [x] [Accordion controls](doc/components/AccordionControls.md)
    - [ ] Dropdown list controls
    - [ ] Autofill input fields
    - [ ] Links inline with text
    - [ ] Dialogs
    - [ ] Bottom sheets
    - [ ] TabBar and Tab controls
- Other
    - [x] [Compose Semantics automated testing](doc/AutomatedComposeAccessibilityTesting.md)

----

Copyright 2023 CVS Health and/or one of its affiliates
