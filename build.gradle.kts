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

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

plugins {
  base
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.detekt)
  alias(libs.plugins.kotlinx.binaryCompatibility)
  alias(libs.plugins.gradleDoctor)
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.anvil) apply false
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.dropbox.dependencyGuard) apply false
  alias(libs.plugins.google.ksp) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.kapt) apply false
  alias(libs.plugins.ktlint) apply false
}

doctor {
  warnWhenNotUsingParallelGC = false
}


allprojects {
  configurations.all {
    resolutionStrategy {
      eachDependency {
        when {
          requested.group == "org.jetbrains.kotlin" -> useVersion(libs.versions.kotlin.get())
        }
      }
    }
  }
}

detekt {

  parallel = true
  config.setFrom(files("$rootDir/detekt/detekt-config.yml"))
}

tasks.withType<DetektCreateBaselineTask> {

  setSource(files(rootDir))

  include("**/*.kt", "**/*.kts")
  exclude("**/resources/**", "**/build/**", "**/src/test/java**")

  // Target version of the generated JVM bytecode. It is used for type resolution.
  this.jvmTarget = "1.8"
}

tasks.withType<Detekt> {

  reports {
    xml.required.set(true)
    html.required.set(true)
    txt.required.set(false)
  }

  setSource(files(projectDir))

  include("**/*.kt", "**/*.kts")
  exclude(
    "**/resources/**",
    "**/build/**",
    "**/src/test/java**",
    "**/src/integrationTest/kotlin**",
    "**/src/test/kotlin**"
  )

  // Target version of the generated JVM bytecode. It is used for type resolution.
  this.jvmTarget = "1.8"
}


apiValidation {
  /** Packages that are excluded from public API dumps even if they contain public API. */
  ignoredPackages.add("tangle.inject.api.internal")

  /** Sub-projects that are excluded from API validation */
  ignoredProjects.addAll(
    listOf(
      "tangle-test-utils",
      "tangle-compiler",
      "tangle-fragment-compiler",
      "tangle-viewmodel-compiler",
      "tangle-work-compiler",
      "app",
      "core",
      "data",
      "ui"
    )
  )

  /**
   * Set of annotations that exclude API from being public. Typically, it is all kinds of
   * `@InternalApi` annotations that mark effectively private API that cannot be actually private
   * for technical reasons.
   */
  nonPublicMarkers.add("tangle.api.internal.InternalTangleApi")
}

// Delete any empty directories while cleaning.
// This is mostly just because IntelliJ/AS likes to randomly create both `/java` and `/kotlin`
// source directories and that annoys me.
allprojects {
  val proj = this@allprojects

  proj.tasks
    .withType<Delete>()
    .configureEach {
      doLast {

        val subprojectDirs = proj.subprojects
          .map { it.projectDir.path }

        proj.projectDir.walkBottomUp()
          .filter { it.isDirectory }
          .filterNot { dir -> subprojectDirs.any { dir.path.startsWith(it) } }
          .filterNot { it.path.contains(".gradle") }
          .filter { it.listFiles()?.isEmpty() != false }
          .forEach { it.deleteRecursively() }
      }
    }
}

// Hack for ensuring that when 'publishToMavenLocal' is invoked from the root project,
// all subprojects are published.  This is used in plugin tests.
val publishToMavenLocal by tasks.registering {
  subprojects.forEach { sub ->
    dependsOn(sub.tasks.matching { it.name == "publishToMavenLocal" })
  }
}
