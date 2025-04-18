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

plugins {
  id("dev.whosnickdoglio.convention.tangle.androidLibrary")
  id("dev.whosnickdoglio.convention.tangle.published")
}

tanglePublishing {
  artifactId.set("tangle-fragment-api")
}

android { namespace = "tangle.fragment.api" }

dependencies {

  api(libs.androidx.fragment.core)
  api(libs.javax.inject)

  api(projects.tangleApi)

  compileOnly(libs.google.auto.service.processor)

  implementation(libs.google.dagger.api)

  testImplementation(libs.junit.junit4)
  testImplementation(libs.kotlin.reflect)
  testImplementation(libs.robolectric)

  testImplementation(projects.tangleApi)
  testImplementation(projects.tangleCompiler)
  testImplementation(projects.tangleFragmentCompiler)
  testImplementation(projects.tangleTestUtils)
  testImplementation(projects.tangleViewmodelApi)
  testImplementation(projects.tangleViewmodelCompiler)
}
