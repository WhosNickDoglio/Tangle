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
import dev.whosnickdoglio.convention.tangle.builds.VERSION_NAME

plugins {
  id("dev.whosnickdoglio.convention.tangle.javaLibrary")
  id("dev.whosnickdoglio.convention.tangle.published")
  alias(libs.plugins.buildConfig)
  `java-gradle-plugin`
  idea
}

tanglePublishing {
  artifactId.set("tangle-gradle-plugin")
}

val main by sourceSets.getting
val test by sourceSets.getting

val integrationTest by java.sourceSets.registering {
  kotlin.apply {
    compileClasspath +=
      main.output
        .plus(test.output)
        .plus(configurations.testRuntimeClasspath.get())
    runtimeClasspath += output + compileClasspath
  }
}

// mark the integrationTest directory as a test directory in the IDE
idea {
  module {
    integrationTest.configure {
      allSource.srcDirs
        .forEach { srcDir ->
          module.testSourceDirs.add(srcDir)
        }
    }
  }
}

dependencies {

  compileOnly(libs.android.gradle)
  compileOnly(libs.kotlin.reflect)

  implementation(libs.kotlin.stdlib.jdk8)

  "integrationTestImplementation"(libs.bundles.jUnit)
  "integrationTestImplementation"(libs.bundles.kotest)

  testImplementation(libs.bundles.jUnit)
  testImplementation(libs.bundles.kotest)
}

gradlePlugin {
  plugins {
    create("tangle") {
      id = "com.rickbusarow.tangle"
      group = "com.rickbusarow.tangle"
      displayName = "Tangle"
      implementationClass = "tangle.inject.gradle.TanglePlugin"
      version = VERSION_NAME
      description = "Create Android component bindings for Dagger with Anvil"
      website.set("https://github.com/RBusarow/Tangle")
      vcsUrl.set("https://github.com/RBusarow/Tangle")
      tags.set(setOf("android", "dagger2", "kotlin", "kotlin-compiler-plugin"))
    }
  }
}

buildConfig {
  forClass("BuildProperties") {
    packageName = "tangle.inject.gradle"
    buildConfigField("VERSION", provider { version.toString() })
    buildConfigField("GROUP", provider { group.toString() })
  }

  sourceSets.named("integrationTest") {
    forClass("TestVersions") {
      packageName = "tangle.inject.gradle"
      buildConfigField("AGP", provider { libs.versions.androidTools.get() })
      buildConfigField("ANVIL", provider { libs.versions.square.anvil.get() })
      buildConfigField("GRADLE", provider { gradle.gradleVersion })
      buildConfigField("KOTLIN", provider { libs.versions.kotlin.get() })
      buildConfigField("ACTIVITY", provider { libs.versions.androidx.activity.get() })
      buildConfigField(
        "FRAGMENT",
        provider { libs.versions.androidx.fragment.version.get() }
      )
      buildConfigField("LIFECYCLE", provider { libs.versions.androidx.lifecycle.get() })
      buildConfigField("COMPOSE", provider { libs.versions.androidx.compose.runtime.get() })
      buildConfigField("WORK", provider { libs.versions.androidx.work.version.get() })
    }
  }
}

tasks.register("setupPluginUploadFromEnvironment") {
  doLast {
    val key = System.getenv("GRADLE_PUBLISH_KEY")
    val secret = System.getenv("GRADLE_PUBLISH_SECRET")

    if (key == null || secret == null) {
      throw GradleException(
        "gradlePublishKey and/or gradlePublishSecret are not defined environment variables"
      )
    }

    System.setProperty("gradle.publish.key", key)
    System.setProperty("gradle.publish.secret", secret)
  }
}

val integrationTestTask =
  tasks.register("integrationTest", Test::class) {
    val integrationTestSourceSet = java.sourceSets["integrationTest"]
    testClassesDirs = integrationTestSourceSet.output.classesDirs
    classpath = integrationTestSourceSet.runtimeClasspath
  }

tasks.matching { it.name == "check" }.all { dependsOn(integrationTestTask) }

kotlin {
  val compilations = target.compilations

  compilations.getByName("integrationTest") {
    associateWith(compilations.getByName("main"))
  }
}
