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

package tangle.fragment.compiler

import com.squareup.anvil.compiler.api.GeneratedFileWithSources
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import tangle.fragment.compiler.FragmentInject_ModuleGenerator.FragmentBindingModuleParams
import tangle.inject.compiler.ClassNames
import tangle.inject.compiler.FileGenerator
import tangle.inject.compiler.addContributesTo
import tangle.inject.compiler.addFunction
import tangle.inject.compiler.applyEach
import tangle.inject.compiler.asArgumentList
import tangle.inject.compiler.buildFile
import tangle.inject.compiler.generateSimpleNameString
import tangle.inject.compiler.wrapInProvider
import java.io.File

/**
 * Creates qualified bindings for the implementations of `@FragmentInjectFactory`-annotated
 * interfaces.
 *
 * ```
 * @Module
 * @ContributesTo(AppScope::class)
 * public object Tangle_Unit_FragmentInject_Module {
 *   @Provides
 *   public fun provide_MyFragment_Factory(): MyFragment.Factory =
 *       MyFragment_Factory_Impl.create(MyFragment_Factory.create()).get()
 * }
 * ```
 */
internal object FragmentInject_ModuleGenerator : FileGenerator<FragmentBindingModuleParams> {
  override fun generate(
    codeGenDir: File,
    params: FragmentBindingModuleParams
  ): GeneratedFileWithSources {
    val scopeClassName = params.scopeClassName
    val packageName = params.packageName

    val moduleName = "Tangle_${scopeClassName.generateSimpleNameString()}_FragmentInject_Module"

    val factoryImpls = params.factoryParams

    val content =
      FileSpec.buildFile(packageName, moduleName) {
        addType(
          TypeSpec.objectBuilder(ClassName(packageName, moduleName))
            .addAnnotation(ClassNames.module)
            .addContributesTo(scopeClassName)
            .applyEach(factoryImpls) { params ->

              val factoryConstructorParams =
                params.fragmentParams.constructorParams + params.fragmentParams.memberInjectedParams

              val args =
                factoryConstructorParams.asArgumentList(
                  asProvider = false,
                  includeModule = false
                )
              addFunction(
                "provide_${params.factoryInterfaceClassName.generateSimpleNameString()}"
              ) {
                addAnnotation(ClassNames.provides)
                factoryConstructorParams.forEach { argument ->
                  addParameter(
                    ParameterSpec.builder(argument.name, argument.typeName.wrapInProvider())
                      .applyEach(argument.qualifiers) { addAnnotation(it) }
                      .build()
                  )
                }
                returns(params.factoryInterfaceClassName)
                addStatement(
                  "return·%T.create(%T.create($args)).get()",
                  params.factoryImplClassName,
                  params.fragmentFactoryClassName
                )
              }
            }
            .build()
        )
      }

    return createGeneratedFileWithSources(
      codeGenDir = codeGenDir,
      packageName = packageName,
      fileName = moduleName,
      content = content,
      sources = params.factoryParams.map { it.factoryClass.containingFileAsJavaFile }.toSet()
    )
  }

  data class FragmentBindingModuleParams(
    val scopeClassName: ClassName,
    val packageName: String,
    val factoryParams: List<FragmentInjectParams.Factory>
  )
}
