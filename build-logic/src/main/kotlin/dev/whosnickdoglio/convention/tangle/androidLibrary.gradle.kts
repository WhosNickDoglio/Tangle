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

package dev.whosnickdoglio.convention.tangle

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("com.autonomousapps.dependency-analysis")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
  jvmToolchain {
    languageVersion.set(
      JavaLanguageVersion.of(
        libs.findVersion("jdk")
          .get().requiredVersion.toInt()
      )
    )
    vendor = JvmVendorSpec.AZUL
  }
}

android {

  commonAndroid(project)
  common()

  // don't generate BuildConfig
  @Suppress("UnstableApiUsage")
  buildFeatures.buildConfig = false
}
