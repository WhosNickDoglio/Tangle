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

package dev.whosnickdoglio.convention.tangle

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
}

common()

java {
  // force Java 8 source when building java-only artifacts.
  // This is different than the Kotlin jvm target.
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

val lintMain by tasks.registering {

  doFirst {
    tasks.withType<KotlinCompile>()
      .configureEach {
        kotlinOptions {
          allWarningsAsErrors = true
        }
      }
  }
}
lintMain {
  finalizedBy("compileKotlin")
}

val testJvm by tasks.registering {
  dependsOn("test")
}

val buildTests by tasks.registering {
  dependsOn("testClasses")
}
