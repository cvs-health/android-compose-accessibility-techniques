plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.cvshealth.a11y"
version = "0.1.0"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("a11yPlugin") {
            id = "com.cvshealth.a11y"
            implementationClass = "com.cvshealth.a11y.gradle.A11yGradlePlugin"
            displayName = "Compose Accessibility Checker"
            description = "WCAG 2.2 static analysis for Jetpack Compose"
        }
    }
}
