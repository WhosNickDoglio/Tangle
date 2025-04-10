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

package tangle.work

import android.content.Context
import androidx.work.WorkerParameters
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestFactory
import tangle.inject.test.utils.BaseTest
import tangle.inject.test.utils.createInstance
import tangle.inject.test.utils.invokeCreate
import tangle.inject.test.utils.invokeGet
import javax.inject.Provider

class WorkerFactoryGeneratorTest : BaseTest() {
  val context = mockk<Context>()
  val workerParameters = mockk<WorkerParameters>()

  @AfterEach
  fun tearDown() {
    unmockkAll()
  }

  @TestFactory
  fun `Worker factory is generated without any additional arguments`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted context: Context,
      @Assisted params: WorkerParameters
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """
      ) {
        val factory = myWorker_FactoryClass.createInstance()
        val assistedFactory = myWorker_AssistedFactoryClass.createInstance()

        factory.invokeGet(context, workerParameters)::class.java shouldBe myWorkerClass
        assistedFactory.invokeCreate(context, workerParameters)::class.java shouldBe myWorkerClass
      }
    }

  @TestFactory
  fun `Worker assisted arguments may have any name`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted orange: Context,
      @Assisted banana: WorkerParameters
    ) : Worker(orange, banana) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """
      ) {
        val factory = myWorker_FactoryClass.createInstance()
        val assistedFactory = myWorker_AssistedFactoryClass.createInstance()

        factory.invokeGet(context, workerParameters)::class.java shouldBe myWorkerClass
        assistedFactory.invokeCreate(context, workerParameters)::class.java shouldBe myWorkerClass
      }
    }

  @TestFactory
  fun `Worker assisted arguments may be in any order`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted params: WorkerParameters,
      @Assisted context: Context
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """
      ) {
        val factory = myWorker_FactoryClass.createInstance()
        val assistedFactory = myWorker_AssistedFactoryClass.createInstance()

        factory.invokeGet(workerParameters, context)::class.java shouldBe myWorkerClass
        assistedFactory.invokeCreate(context, workerParameters)::class.java shouldBe myWorkerClass
      }
    }

  @TestFactory
  fun `Worker factory is generated with an injected argument`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted context: Context,
      @Assisted params: WorkerParameters,
      string: String
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """
      ) {
        val factory = myWorker_FactoryClass.createInstance(Provider { "string" })
        val assistedFactory = myWorker_AssistedFactoryClass.createInstance(Provider { "string" })

        factory.invokeGet(context, workerParameters)::class.java shouldBe myWorkerClass
        assistedFactory.invokeCreate(context, workerParameters)::class.java shouldBe myWorkerClass
      }
    }

  @TestFactory
  fun `Worker may not have additional assisted args`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted context: Context,
      @Assisted params: WorkerParameters,
      @Assisted string: String
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """,
        shouldFail = true
      ) {
        messages shouldContainIgnoringWhitespaces """
        @TangleWorker-annotated classes may only have Context and WorkerParameters as @Assisted-annotated parameters.

          required assisted constructor parameters
          	context: android.content.Context
          	params: androidx.work.WorkerParameters

          actual assisted constructor parameters
          	context: android.content.Context
          	params: androidx.work.WorkerParameters
          	string: kotlin.String
          """
      }
    }

  @TestFactory
  fun `Worker must have WorkerParameters assisted args`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted context: Context,
      params: WorkerParameters
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """,
        shouldFail = true
      ) {
        messages shouldContainIgnoringWhitespaces """
        @TangleWorker-annotated classes may only have Context and WorkerParameters as @Assisted-annotated parameters.

          required assisted constructor parameters
          	context: android.content.Context
          	params: androidx.work.WorkerParameters

          actual assisted constructor parameters
          	context: android.content.Context
          """
      }
    }

  @TestFactory
  fun `Worker must have Context assisted args`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      context: Context,
      @Assisted params: WorkerParameters
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """,
        shouldFail = true
      ) {
        messages shouldContainIgnoringWhitespaces """
        @TangleWorker-annotated classes may only have Context and WorkerParameters as @Assisted-annotated parameters.

          required assisted constructor parameters
          	context: android.content.Context
          	params: androidx.work.WorkerParameters

          actual assisted constructor parameters
          	params: androidx.work.WorkerParameters
          """
      }
    }

  @TestFactory
  fun `qualified inject parameter propagates qualifiers`() =
    test {
      compile(
        """
    package tangle.inject.tests

    import android.content.Context
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedInject
    import tangle.work.TangleWorker
    import javax.inject.Qualifier

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SomeQualifier

    @TangleWorker
    class MyWorker @AssistedInject constructor(
      @Assisted context: Context,
      @Assisted params: WorkerParameters,
      @SomeQualifier
      val qualified: String
    ) : Worker(context, params) {
      override fun doWork(): Result {
        return Result.success()
      }
    }
    """
      ) {
        val clazz = classLoader.loadClass("tangle.inject.tests.SomeQualifier")

        val constructor =
          myWorker_AssistedFactoryClass.kotlin.constructors
            .single()

        val annotationClasses =
          constructor.parameters
            .single { it.name == "qualified" }
            .annotations.map { it.annotationClass }

        annotationClasses shouldContain clazz.kotlin
      }
    }
}
