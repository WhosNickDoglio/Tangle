/*
 * Copyright (C) 2025 Rick Busarow
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

package tangle.inject.gradle

import com.autonomousapps.kit.AbstractGradleProject
import com.autonomousapps.kit.GradleProject
import com.autonomousapps.kit.Source
import com.autonomousapps.kit.gradle.BuildScript
import com.autonomousapps.kit.gradle.BuildscriptBlock
import com.autonomousapps.kit.gradle.Dependencies
import com.autonomousapps.kit.gradle.Dependency
import com.autonomousapps.kit.gradle.Dependency.Companion.androidPlugin
import com.autonomousapps.kit.gradle.GradleProperties
import com.autonomousapps.kit.gradle.Plugin
import com.autonomousapps.kit.gradle.Repositories
import org.intellij.lang.annotations.Language

fun TanglePluginFixture(
  pluginVersions: PluginVersions,
  @Language("kotlin")
  additionalDsl: String,
  dependencies: List<Dependency> = emptyList(),
  sourceFiles: List<Source> = emptyList(),
): TanglePluginFixture =
  DefaultTanglePluginFixture(
    pluginVersions = pluginVersions,
    additionsDsl = additionalDsl,
    additionalDependencies = dependencies,
    sourceFiles = sourceFiles,
  )

data class PluginVersions(
  val gradleVersion: String,
  val agpVersion: String,
  val anvilVersion: String,
  val kotlinVersion: String
)

interface TanglePluginFixture {
  val gradleProject: GradleProject
}

private class DefaultTanglePluginFixture(
  private val pluginVersions: PluginVersions,
  @Language("kotlin")
  private val additionsDsl: String,
  private val additionalDependencies: List<Dependency>,
  private val sourceFiles: List<Source> = emptyList(),
) :
  AbstractGradleProject(), TanglePluginFixture {
  override val gradleProject: GradleProject = build()

  private fun build(): GradleProject =
    newGradleProjectBuilder(GradleProject.DslKind.KOTLIN)
      .withRootProject {
        gradleProperties = GradleProperties.minimalAndroidProperties()
        withBuildScript {
          buildscript =
            BuildscriptBlock(
              repositories = Repositories.DEFAULT_PLUGINS,
              dependencies =
                Dependencies(
                  androidPlugin(pluginVersions.agpVersion),
                  Dependency(
                    "classpath",
                    "com.squareup.anvil:gradle-plugin:${pluginVersions.anvilVersion}"
                  ),
                  Dependency(
                    "classpath",
                    "org.jetbrains.kotlin:kotlin-gradle-plugin:${pluginVersions.kotlinVersion}"
                  )
                )
            )
        }
      }
      .withAndroidSubproject("project") {
        sources = sourceFiles
        buildScript =
          BuildScript.Builder()
            .apply {
              additions = (androidDsl + additionsDsl)
              plugins =
                mutableListOf(
                  Plugin("com.rickbusarow.tangle", PLUGIN_UNDER_TEST_VERSION),
                  Plugin("com.android.library"),
                  Plugin("org.jetbrains.kotlin.android")
                )
              dependencies = additionalDependencies.toMutableList()
            }
            .build()
      }
      .write()

  override fun toString(): String =
    "[gradle='${pluginVersions.gradleVersion}'," +
      " kotlin='${pluginVersions.kotlinVersion}', " +
      "agp='${pluginVersions.agpVersion}', " +
      "anvil='${pluginVersions.anvilVersion}']"
}

// TODO file bug
private val androidDsl =
  """
  android {
    namespace = "foo"
    compileSdk = 34
    defaultConfig {
      minSdk = 21
    }
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
  }

  tasks.register("deps") {
    doLast {
      listOf("anvil", "api", "implementation")
        .forEach { config ->
          project.configurations
            .named(config)
            .get()
            .dependencies
            .forEach { println("${'$'}config ${'$'}{it.group}:${'$'}{it.name}") }
        }
    }
  }

  """.trimIndent()

val activities = Dependency("api", "androidx.activity:activity:${TestVersions.ACTIVITY}")
val compose = Dependency("api", "androidx.compose.ui:ui:${TestVersions.COMPOSE}")
val fragments = Dependency("api", "androidx.fragment:fragment:${TestVersions.FRAGMENT}")
val viewModels =
  Dependency("api", "androidx.lifecycle:lifecycle-viewmodel:${TestVersions.LIFECYCLE}")
val workManager = Dependency("api", "androidx.work:work-runtime:${TestVersions.WORK}")
