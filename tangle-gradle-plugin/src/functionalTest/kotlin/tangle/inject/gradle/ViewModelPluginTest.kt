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

import com.autonomousapps.kit.GradleBuilder
import org.junit.jupiter.api.TestFactory

class ViewModelPluginTest {
  @TestFactory
  fun `viewmodel compiler and api should be automatically enabled with androidx viewmodel dependencies`() =
    test(additionalDependencies = listOf(viewModels)) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api"
        )
    }

  @TestFactory
  fun `viewmodel activity api should be automatically enabled with androidx viewmodel and activity dependencies`() =
    test(additionalDependencies = listOf(activities, viewModels)) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-activity",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api"
        )
    }

  @TestFactory
  fun `viewmodel fragment api should be automatically enabled with androidx viewmodel and fragment dependencies`() =
    test(additionalDependencies = listOf(fragments, viewModels)) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-fragment-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-fragment-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-fragment"
        )
    }

  @TestFactory
  fun `disabling viewModels in config should disable its dependencies`() =
    test(
      additionalDependencies = listOf(activities, fragments, viewModels, compose, workManager),
      dslAdditions =
        """
        tangle {
          viewModelOptions {
            enabled = false // default is null
          }
        }
        """.trimIndent()
    ) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-fragment-compiler",
          "anvil com.rickbusarow.tangle:tangle-work-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-fragment-api",
          "implementation com.rickbusarow.tangle:tangle-work-api"
        )
    }

  @TestFactory
  fun `disabling viewModels fragments in config should disable the viewmodel fragment api dependency`() =
    test(
      additionalDependencies = listOf(activities, fragments, viewModels, compose, workManager),
      dslAdditions =
        """
        tangle {
          viewModelOptions {
            fragmentsEnabled = false // default is null
          }
        }
        """.trimIndent()
    ) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-fragment-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "anvil com.rickbusarow.tangle:tangle-work-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-fragment-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-activity",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-compose",
          "implementation com.rickbusarow.tangle:tangle-work-api"
        )
    }

  @TestFactory
  fun `disabling viewModels activities in config should disable the viewmodel activity api dependency`() =
    test(
      additionalDependencies = listOf(activities, fragments, viewModels, compose, workManager),
      dslAdditions =
        """
        tangle {
          viewModelOptions {
            activitiesEnabled = false // default is null
          }
        }
        """.trimIndent()
    ) {
      GradleBuilder.build(it.rootDir, "deps").tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-fragment-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "anvil com.rickbusarow.tangle:tangle-work-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-fragment-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-compose",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-fragment",
          "implementation com.rickbusarow.tangle:tangle-work-api"
        )
    }
}
