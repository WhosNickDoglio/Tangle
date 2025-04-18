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

package tangle.inject.compiler.components.fileGen

import com.squareup.anvil.compiler.api.GeneratedFileWithSources
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import tangle.inject.compiler.ClassNames
import tangle.inject.compiler.FileGenerator
import tangle.inject.compiler.addContributesTo
import tangle.inject.compiler.addFunction
import tangle.inject.compiler.buildFile
import tangle.inject.compiler.components.MergeComponentParams
import tangle.inject.compiler.generateSimpleNameString
import tangle.inject.compiler.tangleScopeMap
import java.io.File

/**
 * ```
 * @Module
 * @ContributesTo(UserScope::class)
 * public interface Default_InjectedClassToScopeClass_Module {
 *   @Multibinds
 *   @TangleScopeMap
 *   public fun bindTangleScopeMap(): Map<Class<*>, Class<*>>
 * }
 * ```
 */
internal object UserScope_Default_InjectedClassToScopeClass_Module_Generator :
  FileGenerator<MergeComponentParams> {
  override fun generate(
    codeGenDir: File,
    params: MergeComponentParams
  ): GeneratedFileWithSources {
    val packageName = params.packageName

    val scopeName = params.scopeClassName.generateSimpleNameString()

    val classNameString = "${scopeName}_Default_InjectedClassToScopeClass_Module"
    val className = ClassName(packageName, classNameString)

    val content =
      FileSpec.buildFile(packageName, classNameString) {
        TypeSpec.interfaceBuilder(className)
          .addAnnotation(ClassNames.module)
          .addContributesTo(params.scopeClassName)
          .addFunction("bindTangleScopeMap") {
            addAnnotation(ClassNames.tangleScopeMap)
            addAnnotation(ClassNames.multibinds)
            addModifiers(KModifier.ABSTRACT)
            returns(ClassNames.javaClassToClassMap)
          }
          .build()
          .let { addType(it) }
      }

    return createGeneratedFileWithSources(
      codeGenDir,
      packageName,
      classNameString,
      content,
      sources = params.sources
    )
  }
}
