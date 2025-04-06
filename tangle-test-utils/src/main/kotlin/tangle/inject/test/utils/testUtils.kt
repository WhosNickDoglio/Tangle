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

package tangle.inject.test.utils

import com.tschuchort.compiletesting.JvmCompilationResult
import dagger.internal.Factory
import java.lang.reflect.Executable
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import javax.inject.Provider
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

val Member.isStatic: Boolean
  get() = Modifier.isStatic(modifiers)

/**
 * Creates a new instance of this class with the given arguments. This method assumes that this
 * class only declares a single constructor.
 */
@Suppress("UNCHECKED_CAST", "NewApi")
fun <T : Any> Class<T>.createInstance(vararg initargs: Any?): T =
  declaredConstructors.single()
    .use { it.newInstance(*initargs) } as T

@Suppress("UNCHECKED_CAST")
fun <T : Any> Class<T>.newInstanceStatic(vararg initargs: Any?): T =
  declaredMethods.filter { it.isStatic }
    .single { it.name == "newInstance" }
    .invoke(null, *initargs) as T

@Suppress("UNCHECKED_CAST")
fun <T : Any> Class<T>.createStatic(vararg initargs: Any?): T =
  declaredMethods.filter { it.isStatic }
    .single { it.name == "create" }
    .invoke(null, *initargs) as T

@Suppress("UNCHECKED_CAST")
fun <T> T.factoryGet(): Any = (this as Factory<*>).get()

@Suppress("UNCHECKED_CAST")
fun JvmCompilationResult.appComponentFactoryCreate(vararg initargs: Any?): Any {
  return daggerAppComponent.factoryFunction()
    .invoke(null)
    .invokeCreate(*initargs)
}

inline fun <T, E : Executable> E.use(block: (E) -> T): T {
  // Deprecated since Java 9, but many projects still use JDK 8 for compilation.
  @Suppress("DEPRECATION")
  val original = isAccessible
  return try {
    isAccessible = true
    block(this)
  } finally {
    isAccessible = original
  }
}

val JvmCompilationResult.appComponentFactory: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.AppComponent\$Factory")

val JvmCompilationResult.appComponent: KClass<out Any>
  get() = classLoader.loadClass("tangle.inject.tests.AppComponent").kotlin

val JvmCompilationResult.daggerAppComponent: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.DaggerAppComponent")

val JvmCompilationResult.targetClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.Target")

val JvmCompilationResult.baseClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.Base")

val JvmCompilationResult.myViewModelClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyViewModel")

val JvmCompilationResult.myFragmentClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyFragment")

val JvmCompilationResult.myFragmentFactoryImplClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyFragment_Factory_Impl")

val JvmCompilationResult.tangleUnitFragmentModuleClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.Tangle_Unit_Fragment_Module")

val JvmCompilationResult.tangleUnitFragmentInjectModuleClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.Tangle_Unit_FragmentInject_Module")

val JvmCompilationResult.tangleUnitFragmentModuleCompanionClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.Tangle_Unit_Fragment_Module\$Companion")

val JvmCompilationResult.bindMyFragment: Method
  get() = tangleUnitFragmentModuleClass.getDeclaredMethod("bind_MyFragment", myFragmentClass)

val JvmCompilationResult.provideMyFragment: Method
  get() = tangleUnitFragmentModuleCompanionClass.getDeclaredMethod("provide_MyFragment")

fun Method.annotationClasses() = annotations.map { it.annotationClass }

fun Class<*>.annotationClasses() = annotations.map { it.annotationClass }

fun <T : Any> KClass<T>.property(name: String) =
  memberProperties
    .first { it.name == name }

fun <T> Any.propertyValue(name: String): T {
  val property = this::class.property(name)
  @Suppress("UNCHECKED_CAST")
  return property.call(this) as T
}

fun Any.provider() = Provider { this }
