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

import com.android.build.api.dsl.ApplicationBaseFlavor
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UnstableApiUsage", "MagicNumber")
fun CommonExtension<*, *, *, *, *, *>.commonAndroid(target: Project) {

  val publishedAsArtifact = target.extensions.findByName("com.vanniktech.maven.publish") != null

  compileSdk = 35

  defaultConfig {
    minSdk = 21

    // `targetSdk` doesn't have a single base interface, as of AGP 7.1.0
    when (this@defaultConfig) {
      is ApplicationBaseFlavor -> targetSdk = 35
    }

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes.configureEach {
    isMinifyEnabled = false
    proguardFiles(
      getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
    )
  }

  compileOptions {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
  }

  sourceSets {
    findByName("androidTest")?.java?.srcDirs("src/androidTest/kotlin")
    findByName("main")?.java?.srcDirs("src/main/kotlin")
    findByName("test")?.java?.srcDirs("src/test/kotlin")
  }

  if (publishedAsArtifact) {
    lint {
      disable.addAll(setOf("ObsoleteLintCustomCheck", "MissingTranslation"))
      enable.addAll(setOf("InvalidPackage", "Interoperability"))
      abortOnError = true
      checkDependencies = true
      checkAllWarnings = true
    }
  }

  testOptions {
    unitTests.isIncludeAndroidResources = true
    unitTests.isReturnDefaultValues = true
    animationsDisabled = true
  }
  target.tasks.register("lintMain") {
    doFirst {
      target.tasks.withType<KotlinCompile>()
        .configureEach {
          kotlinOptions {
            allWarningsAsErrors = true

            if (publishedAsArtifact) {
              freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
            }
          }
        }
    }

    finalizedBy("lintDebug")
  }

  target.tasks.register("testJvm") {

    dependsOn("testDebugUnitTest")
  }

  target.tasks.register("buildTests") {

    dependsOn("assembleDebugUnitTest")
  }

  // explicit API mode doesn't work in the IDE for Android projects
  // https://youtrack.jetbrains.com/issue/KT-37652
  // disabling this bandaid because it also complains about exlicit API things in test sources
  /*
  target.tasks
    .matching { it is KotlinCompile }
    .configureEach {
      val task = this
      val shouldEnable = !task.name.contains("test", ignoreCase = true)
      val kotlinCompile = task as KotlinCompile

      if (shouldEnable && !project.hasProperty("kotlin.optOutExplicitApi")) {
        if ("-Xexplicit-api=strict" !in kotlinCompile.kotlinOptions.freeCompilerArgs) {
          kotlinCompile.kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
        }
      } else {
        kotlinCompile.kotlinOptions.freeCompilerArgs = kotlinCompile.kotlinOptions
          .freeCompilerArgs
          .filterNot { it == "-Xexplicit-api=strict" }
      }
    }
  */
}
