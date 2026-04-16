/*
 * Copyright 2026 CVS Health
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.a11y.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * DSL extension for configuring the Compose Accessibility Checker.
 *
 * Example usage in a consuming project's build.gradle.kts:
 * ```kotlin
 * a11y {
 *     minScore.set(80)
 *     format.set("sarif")
 *     paths.set(listOf("src/main/java", "src/main/kotlin"))
 *     failOnError.set(true)
 *     disable.set(listOf("ContentDescriptionCheck"))
 * }
 * ```
 */
abstract class A11yExtension @Inject constructor(objects: ObjectFactory) {

    /**
     * Minimum accessibility score (0–100) required to pass. Defaults to 0.
     */
    val minScore: Property<Int> = objects.property(Int::class.java).convention(0)

    /**
     * Output format for the accessibility report. Defaults to "gradle".
     * Other supported values: "sarif", "json".
     */
    val format: Property<String> = objects.property(String::class.java).convention("gradle")

    /**
     * Source paths to scan for Compose UI code. Defaults to ["src/main/java"].
     */
    val paths: ListProperty<String> =
        objects.listProperty(String::class.java).convention(listOf("src/main/java"))

    /**
     * Whether to fail the build when accessibility violations are found. Defaults to true.
     */
    val failOnError: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    /**
     * List of check IDs to disable. Defaults to empty (all checks enabled).
     */
    val disable: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())
}
