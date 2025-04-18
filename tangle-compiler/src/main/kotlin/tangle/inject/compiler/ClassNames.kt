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

package tangle.inject.compiler

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeComponent
import com.squareup.anvil.annotations.MergeSubcomponent
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.internal.Factory
import dagger.internal.InstanceFactory
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import dagger.multibindings.StringKey
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

object ClassNames {
  val androidContext = ClassName("android.content", "Context")
  val string = String::class.asClassName()

  val internalTangleApi = ClassName("tangle.inject", "InternalTangleApi")

  val tangleAppScope = ClassName("tangle.inject.internal", "TangleAppScope")
  val tangleScope = ClassName("tangle.inject", "TangleScope")

  val provider = Provider::class.asClassName()

  val javaClassWildcard =
    Class::class.asClassName()
      .parameterizedBy(TypeVariableName("*"))

  val javaClassToClassMap =
    Map::class.asClassName()
      .parameterizedBy(javaClassWildcard, javaClassWildcard)

  val binds = Binds::class.asClassName()
  val bindsInstance = BindsInstance::class.asClassName()
  val classKey = ClassKey::class.asClassName()
  val contributesTo = ContributesTo::class.asClassName()
  val daggerFactory = Factory::class.asClassName()
  val intoMap = IntoMap::class.asClassName()
  val intoSet = IntoSet::class.asClassName()
  val mergeComponent = MergeComponent::class.asClassName()
  val mergeSubcomponent = MergeSubcomponent::class.asClassName()
  val module = Module::class.asClassName()
  val multibinds = Multibinds::class.asClassName()
  val named = Named::class.asClassName()
  val provides = Provides::class.asClassName()
  val stringKey = StringKey::class.asClassName()
  val subcomponentFactory = Subcomponent.Factory::class.asClassName()

  val instanceFactory = InstanceFactory::class.asClassName()

  val inject = Inject::class.asClassName()
  val jvmStatic = JvmStatic::class.asClassName()

  val bundle = ClassName("android.os", "Bundle")
  val iBinder = ClassName("android.os", "IBinder")
  val parcelable = ClassName("android.os", "Parcelable")
  val size = ClassName("android.util", "Size")
  val sizeF = ClassName("android.util", "SizeF")
}
