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

class ComposePluginTest {
  @TestFactory
  fun `viewmodel compose api should be automatically enabled with androidx viewmodel and compose dependencies`() =
    test(
      additionalDependencies =
        listOf(
          compose,
          viewModels
        )
    ) { project ->

      val result = GradleBuilder.build(project.rootDir, "deps")

      result.tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-compose"
        )
    }

  @TestFactory
  fun `disabling viewModels compose in config should disable the viewmodel compose api dependency`() =
    test(
      dslAdditions =
        """
        tangle {
            viewModelOptions {
              composeEnabled = false // default is null
            }
          }
        """.trimIndent(),
      additionalDependencies =
        listOf(
          activities,
          compose,
          fragments,
          viewModels,
          workManager
        )
    ) { projec ->
      val result = GradleBuilder.build(projec.rootDir, "deps")

      result.tangleDeps() shouldBe
        listOf(
          "anvil com.rickbusarow.tangle:tangle-compiler",
          "anvil com.rickbusarow.tangle:tangle-fragment-compiler",
          "anvil com.rickbusarow.tangle:tangle-viewmodel-compiler",
          "anvil com.rickbusarow.tangle:tangle-work-compiler",
          "implementation com.rickbusarow.tangle:tangle-api",
          "implementation com.rickbusarow.tangle:tangle-fragment-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-activity",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-api",
          "implementation com.rickbusarow.tangle:tangle-viewmodel-fragment",
          "implementation com.rickbusarow.tangle:tangle-work-api"
        )
    }
}
