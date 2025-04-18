/*
 * Copyright (C) 2022 Rick Busarow
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
}

dependencyResolutionManagement {
  repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.develocity") version "3.19.2"
  id("com.gradle.common-custom-user-data-gradle-plugin") version "2.2.1"
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

val isCI = providers.environmentVariable("CI").isPresent

develocity {
  buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    termsOfUseAgree = "yes"
    uploadInBackground = !isCI
  }
}

rootProject.name = "Tangle"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
  ":sample:app",
  ":sample:core",
  ":sample:data",
  ":sample:ui",
  ":tangle-api",
  ":tangle-compiler",
  ":tangle-fragment-api",
  ":tangle-fragment-compiler",
  ":tangle-gradle-plugin",
  ":tangle-test-utils",
  ":tangle-viewmodel-activity",
  ":tangle-viewmodel-api",
  ":tangle-viewmodel-compiler",
  ":tangle-viewmodel-compose",
  ":tangle-viewmodel-fragment",
  ":tangle-work-api",
  ":tangle-work-compiler"
)
