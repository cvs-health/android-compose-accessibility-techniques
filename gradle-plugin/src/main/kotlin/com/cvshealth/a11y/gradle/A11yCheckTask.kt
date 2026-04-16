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
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.JavaExecSpec
import javax.inject.Inject

abstract class A11yCheckTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputFile
    abstract val jarFile: RegularFileProperty

    @get:Input
    abstract val paths: ListProperty<String>

    @get:Input
    abstract val format: Property<String>

    @get:Input
    abstract val minScore: Property<Int>

    @get:Input
    abstract val failOnError: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val disable: ListProperty<String>

    @TaskAction
    fun run() {
        val jar = jarFile.get().asFile
        if (!jar.exists()) {
            throw GradleException(
                "A11yAgent JAR not found at: ${jar.absolutePath}\n" +
                "Build it first with: ./gradlew :A11yAgent:shadowJar"
            )
        }

        val cliArgs = mutableListOf<String>()
        cliArgs.addAll(paths.get())
        cliArgs.addAll(listOf("--format", format.get()))

        val score = minScore.get()
        if (score > 0) {
            cliArgs.addAll(listOf("--min-score", score.toString()))
        }

        val disabledChecks = disable.getOrElse(emptyList())
        if (disabledChecks.isNotEmpty()) {
            cliArgs.addAll(listOf("--disable", disabledChecks.joinToString(",")))
        }

        logger.lifecycle("Running A11y accessibility check...")

        val jarRef = jar
        val argsRef = cliArgs.toList()

        val result = execOperations.javaexec(object : Action<JavaExecSpec> {
            override fun execute(spec: JavaExecSpec) {
                spec.classpath(jarRef)
                spec.mainClass.set("com.cvshealth.a11y.agent.cli.A11yCheckKt")
                spec.args(argsRef)
                spec.isIgnoreExitValue = true
            }
        })

        if (result.exitValue != 0) {
            val message = "A11y accessibility check failed with exit code ${result.exitValue}."
            if (failOnError.get()) {
                throw GradleException(message)
            } else {
                logger.warn(message)
            }
        } else {
            logger.lifecycle("A11y accessibility check passed.")
        }
    }
}
