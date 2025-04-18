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

import com.autonomousapps.kit.GradleProject
import com.autonomousapps.kit.gradle.Dependency
import com.autonomousapps.kit.Source
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestInfo
import kotlin.properties.Delegates

val agpVersions = setOf(TestVersions.AGP)
val anvilVersions = setOf(TestVersions.ANVIL)
val gradleVersions = setOf(TestVersions.GRADLE)
val kotlinVersions = setOf(TestVersions.KOTLIN, "1.9.24")

abstract class BasePluginTest {
  protected val activities = "api(\"androidx.activity:activity:${TestVersions.ACTIVITY}\")"
  protected val compose = "api(\"androidx.compose.ui:ui:${TestVersions.COMPOSE}\")"
  protected val fragments = "api(\"androidx.fragment:fragment:${TestVersions.FRAGMENT}\")"
  protected val viewModels =
    "api(\"androidx.lifecycle:lifecycle-viewmodel:${TestVersions.LIFECYCLE}\")"
  protected val workManager = "api(\"androidx.work:work-runtime:${TestVersions.WORK}\")"

  @PublishedApi
  internal var testInfo: TestInfo by Delegates.notNull()

  // This is automatically injected by JUnit5
  @BeforeEach
  internal fun injectTestInfo(testInfo: TestInfo) {
    this.testInfo = testInfo
  }

  inline fun test(crossinline action: TestScope.() -> Unit): List<DynamicTest> =
    gradleVersions.flatMap { gradle ->
      kotlinVersions.flatMap { kotlin ->
        agpVersions.flatMap { agp ->
          anvilVersions.map { anvil ->
            val scope =
              TestScope(
                testInfo = testInfo,
                gradleVersion = gradle,
                kotlinVersion = kotlin,
                agpVersion = agp,
                anvilVersion = anvil
              )
            DynamicTest.dynamicTest(scope.toString()) {
              scope.testProjectDir.deleteRecursively()
              action.invoke(scope)
            }
          }
        }
      }
    }
}

inline fun test(
  @Language("kotlin")
  dslAdditions: String = "",
  additionalDependencies: List<Dependency> = emptyList(),
  sourceFiles: List<Source> = emptyList(),
  crossinline action: (project: GradleProject) -> Unit
): List<DynamicTest> =
  gradleVersions.flatMap { gradle ->
    kotlinVersions.flatMap { kotlin ->
      agpVersions.flatMap { agp ->
        anvilVersions.map { anvil ->
          val fixture =
            TanglePluginFixture(
              pluginVersions =
                PluginVersions(
                  gradleVersion = gradle,
                  kotlinVersion = kotlin,
                  agpVersion = agp,
                  anvilVersion = anvil
                ),
              additionalDsl = dslAdditions,
              dependencies = additionalDependencies,
              sourceFiles = sourceFiles,
            )
          DynamicTest.dynamicTest(fixture.toString()) {
            action(fixture.gradleProject)
          }
        }
      }
    }
  }
