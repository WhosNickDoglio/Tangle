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

package tangle.viewmodel.compiler.components

import com.squareup.anvil.compiler.api.GeneratedFileWithSources
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import tangle.inject.compiler.AnnotationSpec
import tangle.inject.compiler.ClassNames
import tangle.inject.compiler.FileGenerator
import tangle.inject.compiler.buildFile
import tangle.viewmodel.compiler.tangleViewModelComponent
import java.io.File

class ViewModelComponentGenerator : FileGenerator<MergeComponentParams> {
  override fun generate(
    codeGenDir: File,
    params: MergeComponentParams
  ): GeneratedFileWithSources {
    val packageName = params.packageName

    val className = params.componentClassName
    val classNameString = className.simpleName

    val content =
      FileSpec.buildFile(packageName, classNameString) {
        TypeSpec.interfaceBuilder(className)
          .addSuperinterface(ClassNames.tangleViewModelComponent)
          .addAnnotation(
            AnnotationSpec(ClassNames.contributesTo) {
              addMember("%T::class", params.scopeClassName)
            }
          )
          .build()
          .let { addType(it) }
      }

    return createGeneratedFileWithSources(
      codeGenDir,
      packageName,
      classNameString,
      content,
      sources = params.sourceFiles
    )
  }
}
