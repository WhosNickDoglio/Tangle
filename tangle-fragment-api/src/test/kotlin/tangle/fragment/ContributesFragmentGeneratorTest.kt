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

package tangle.fragment

import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.TestFactory
import tangle.inject.test.utils.BaseTest
import tangle.inject.test.utils.annotationClasses
import tangle.inject.test.utils.bindMyFragment
import tangle.inject.test.utils.myFragmentClass
import tangle.inject.test.utils.provideMyFragment
import tangle.inject.test.utils.tangleUnitFragmentModuleClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions

class ContributesFragmentGeneratorTest : BaseTest() {
  @TestFactory
  fun `regular inject annotation gets unqualified map binding`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import javax.inject.Inject

      @ContributesFragment(Unit::class)
      class MyFragment @Inject constructor() : Fragment()
      """
      ) {
        bindMyFragment.annotationClasses() shouldContainExactly
          listOf(
            Binds::class,
            IntoMap::class,
            FragmentKey::class
          )

        bindMyFragment.getAnnotation(FragmentKey::class.java)!!.value shouldBe myFragmentClass.kotlin
      }
    }

  @TestFactory
  fun `FragmentInject annotation gets qualified map binding`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import tangle.inject.TangleParam

      @ContributesFragment(Unit::class)
      class MyFragment @FragmentInject constructor() : Fragment() {

        @FragmentInjectFactory
        interface Factory {
          fun create(@TangleParam("name") name: String): MyFragment
        }
      }
      """
      ) {
        bindMyFragment.annotationClasses() shouldContainExactly
          listOf(
            Binds::class,
            IntoMap::class,
            FragmentKey::class,
            TangleFragmentProviderMap::class
          )

        bindMyFragment.getAnnotation(FragmentKey::class.java)!!.value shouldBe myFragmentClass.kotlin
      }
    }

  @TestFactory
  fun `FragmentInject annotation gets provider function`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import tangle.inject.TangleParam

      @ContributesFragment(Unit::class)
      class MyFragment @FragmentInject constructor() : Fragment() {

        @FragmentInjectFactory
        interface Factory {
          fun create(@TangleParam("name") name: String): MyFragment
        }
      }
      """
      ) {
        provideMyFragment.annotationClasses() shouldContainExactly
          listOf(
            Provides::class,
            TangleFragmentProviderMap::class
          )

        val moduleClass = tangleUnitFragmentModuleClass.kotlin

        moduleClass.companionObject!!.functions
          .first { it.name == "provide_MyFragment" }
          .call(moduleClass.companionObjectInstance)!!::class.java shouldBe myFragmentClass
      }
    }

  @TestFactory
  fun `module scope should match contributed scope`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import javax.inject.Inject

      @ContributesFragment(Unit::class)
      class MyFragment @Inject constructor() : Fragment()
      """
      ) {
        tangleUnitFragmentModuleClass.annotationClasses() shouldContainExactly
          listOf(
            Module::class,
            ContributesTo::class,
            Metadata::class
          )

        tangleUnitFragmentModuleClass
          .getAnnotation(ContributesTo::class.java)!!.scope shouldBe Unit::class
      }
    }

  @TestFactory
  fun `annotated class may extend an abstract base fragment`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import javax.inject.Inject

      @ContributesFragment(Unit::class)
      class Subject @Inject constructor() : BaseFragment()

      abstract class BaseFragment : Fragment()
      """
      ) {
        tangleUnitFragmentModuleClass.annotationClasses() shouldContainExactly
          listOf(
            Module::class,
            ContributesTo::class,
            Metadata::class
          )

        tangleUnitFragmentModuleClass
          .getAnnotation(ContributesTo::class.java)!!.scope shouldBe Unit::class
      }
    }

  @TestFactory
  fun `annotated class must extend fragment`() =
    test {
      compile(
        """
      package tangle.inject.tests

      import androidx.fragment.app.Fragment
      import tangle.fragment.*
      import javax.inject.Inject

      @ContributesFragment(Unit::class)
      class Subject @Inject constructor()
      """,
        shouldFail = true
      ) {
        messages shouldContainIgnoringWhitespaces "The annotation " +
          "`@ContributesFragment(Unit::class)` can only be applied " +
          "to classes which extend androidx.fragment.app.Fragment"
      }
    }
}
