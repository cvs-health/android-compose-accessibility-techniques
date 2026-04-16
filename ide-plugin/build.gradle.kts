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

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

// Path to local Android Studio installation — no network download needed.
// Override via gradle.properties: androidStudioPath=/your/path/Android Studio.app/Contents
val androidStudioPath: String = providers.gradleProperty("androidStudioPath")
    .getOrElse("/Users/c640728/Applications/Android Studio.app/Contents")

repositories {
    mavenCentral()
    intellijPlatform {
        localPlatformArtifacts()
        // JetBrains repos needed for java-compiler-ant-tasks build dependency.
        // If blocked by corporate proxy, import the CVS CA certificate into JDK trust store:
        //   keytool -importcert -file cvs-ca.pem -keystore $JAVA_HOME/lib/security/cacerts -alias cvs-proxy
        defaultRepositories()
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "com.cvshealth.a11y.compose-checker"
        name = "Compose Accessibility Checker"
        version = "0.3.0"
        ideaVersion {
            sinceBuild = "253"
        }
    }
    buildSearchableOptions = false

    // Publish to JetBrains Marketplace:
    //   1. Get a token at https://plugins.jetbrains.com/author/me/tokens
    //   2. Run: ./gradlew publishPlugin -Dintellij.publish.token=YOUR_TOKEN
    //   Or set ORG_GRADLE_PROJECT_intellijPublishToken env var
    publishing {
        token = providers.gradleProperty("intellijPublishToken")
    }
}

dependencies {
    intellijPlatform {
        local(androidStudioPath)
    }
    // Use the A11yAgent shadow JAR — build it first with: ./gradlew :A11yAgent:shadowJar
    implementation(files("../A11yAgent/build/libs/a11y-check-android-0.1.0.jar"))
}
