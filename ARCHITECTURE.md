# android-compose-accessibility-techniques Architecture

## Overall app architecture

android-compose-accessibility-techniques is a single-Activity app with separate screens represented by Composables and orchestrated using the Jetpack Navigation component.

Screen state is held either locally using `remember` or `rememberSaveable`, or held and controlled by ViewModels and immutable data classes, and propagated to the Composables using Kotlin StateFlows. In these ways, unidirectional data flow is maintained, and the Model-View-ViewModel architecture applied where appropriate.

There is no significant business logic in the system, so no domain layer classes exists.

No data is communicated with any back-end system or stored on-device, so the few persistence and data layer classes consist only of hard-coded Kotlin objects.

All layers for a screen are combined in a single package for that screen.

## File structure

The file tree layout of android-compose-accessibility-techniques follows standard Android project structure with package-per-screen organization. All code files relevant to a screen are in the same package: Composables, ViewModel, etc, except for reusable Composables which are held by the ui/components package.

The key file locations are (in alphabetical tree order):

- app/src
    - androidTest/java/com/cvshealth/accessibility/apps/composeaccessibilitytechniques -- contains Composable jUnit UI test files
        - TestHelpers.kt which holds reusable testing functions
    - main/java/com/cvshealth/accessibility/apps/composeaccessibilitytechniques
        - MainActivity.kt
        - ComposeAccessibilityTechniquesRoute.kt -- enum class containing all Navigation Route strings
        - ui -- contains package folders for each screen's code
        - ui/components -- contains reusable components
    - main/res
        - drawable -- contains icons used in the app
        - values/string.xml -- the key external string file
- doc -- contains Markdown documentation of accessibility techniques and project images

## Adding a new screen

The following process is suggested for adding a new screen to the application. This section lists the key app files you will modify most often and the types of files most used in the app. Other file types, such as specific resource files, are added or edited as appropriate for specific screens.

1. Add initial screen strings to res/values/strings.xml. In particular, define a <screen\_name\>\_title string resource containing the screen title and Home screen button label.
2. Create the new app/main/java/com/cvshealth/accessibility/apps/androidviewaccessibilitytechniques/ui/\<screenname\> package folder.
3. In the new package, create the new \<ScreenName\>.kt Composable code.
4. If necessary, in the same package, create \<ScreenName\>ViewModel.kt to hold state data and/or \<ScreenName\>Model.kt to hold data classes and objects.
5. Add a new route enum to ComposeAccessibilityTechniquesRoute.kt holding the new Composable screen route string.
6. Edit MainActivity.kt to add a new composable() navigation to ComposeAccessibilityTechniquesNavHost(). 
7. Edit HomeScreen.kt to copy a NavigationCard, adjust it to the new screen name, and place it in the appropriate layout location. 
8. Build, run, and test the app.