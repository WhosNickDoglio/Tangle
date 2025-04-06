
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
  `kotlin-dsl`
  `java-gradle-plugin`
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get().toInt()))
    vendor = JvmVendorSpec.AZUL
  }
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "dev.whosnickdoglio.convention.tangle.androidApplication"
      implementationClass = "dev.whosnickdoglio.convention.tangle.AndroidAppPlugin"
    }
  }
}


dependencies {
  implementation(platform(libs.kotlin.bom))

  compileOnly(gradleApi())

  implementation(libs.kotlin.annotation.processing)
  implementation(libs.kotlin.compiler)
  implementation(libs.kotlin.gradle.plug)
  implementation(libs.kotlin.gradle.pluginApi)
  implementation(libs.ktlint.gradle)

  implementation(libs.vanniktech.publish)
  implementation(libs.android.gradle)
  implementation(libs.dependencyAnalysis.gradle)
  implementation(libs.dropbox.dependencyGuard)
}
