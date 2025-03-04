/*
 * Copyright (C) 2021 Rick Busarow
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

import com.tschuchort.compiletesting.JvmCompilationResult

val JvmCompilationResult.myWorkerClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyWorker")

val JvmCompilationResult.myWorker_FactoryClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyWorker_Factory")

val JvmCompilationResult.myWorker_AssistedFactoryClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyWorker_AssistedFactory")

val JvmCompilationResult.myWorker_AssistedFactory_FactoryClass: Class<*>
  get() = classLoader.loadClass("tangle.inject.tests.MyWorker_AssistedFactory_Factory")
