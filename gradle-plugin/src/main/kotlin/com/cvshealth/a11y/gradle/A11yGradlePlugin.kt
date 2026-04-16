/*
 * Copyright 2026 CVS Health and/or one of its affiliates
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

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class A11yGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("a11y", A11yExtension::class.java)
        val jarFile = locateShadowJar(project)

        val taskProvider = project.tasks.register("a11yCheck", A11yCheckTask::class.java)
        taskProvider.configure(object : Action<A11yCheckTask> {
            override fun execute(task: A11yCheckTask) {
                task.group = "verification"
                task.description = "Run WCAG 2.2 accessibility checks on Compose source files"
                task.jarFile.set(jarFile)
                task.paths.set(extension.paths)
                task.format.set(extension.format)
                task.minScore.set(extension.minScore)
                task.failOnError.set(extension.failOnError)
                task.disable.set(extension.disable)
            }
        })
    }

    private fun locateShadowJar(project: Project): File {
        val rootDir = project.rootProject.projectDir
        val candidates = listOf(
            File(rootDir.parentFile, "A11yAgent/build/libs"),
            File(rootDir, "A11yAgent/build/libs")
        )
        for (dir in candidates) {
            if (dir.isDirectory) {
                val jar = dir.listFiles()
                    ?.filter { it.extension == "jar" && it.name.startsWith("a11y-check-android") }
                    ?.maxByOrNull { it.lastModified() }
                if (jar != null) return jar
            }
        }
        return File(candidates[1], "a11y-check-android-0.1.0.jar")
    }
}
