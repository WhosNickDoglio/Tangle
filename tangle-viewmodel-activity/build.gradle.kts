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

android { namespace = "tangle.viewmodel.activity" }

tanglePublishing {
  artifactId.set("tangle-viewmodel-activity")
}

dependencies {

  api(libs.androidx.activity.core)
  api(libs.androidx.lifecycle.viewModel.core)
  api(libs.google.dagger.api)

  api(projects.tangleApi)
  api(projects.tangleViewmodelApi)

  implementation(libs.androidx.lifecycle.viewModel.ktx)
  implementation(libs.androidx.savedstate)

  testCompileOnly(libs.google.auto.service.processor)

  testImplementation(projects.tangleApi)
  testImplementation(projects.tangleCompiler)
  testImplementation(projects.tangleFragmentApi)
  testImplementation(projects.tangleFragmentCompiler)
  testImplementation(projects.tangleTestUtils)
  testImplementation(projects.tangleViewmodelCompiler)
}
