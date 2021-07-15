package tangle.inject.compiler

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeComponent
import com.squareup.anvil.annotations.MergeSubcomponent
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.jvm.jvmSuppressWildcards
import dagger.*
import dagger.internal.Factory
import dagger.internal.InstanceFactory
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import tangle.inject.annotations.internal.InternalTangleApi
import tangle.inject.annotations.internal.TangleFragmentProviderMap
import tangle.inject.annotations.internal.TangleScope
import tangle.inject.annotations.internal.TangleViewModelProviderMap
import javax.inject.Inject
import javax.inject.Provider

@OptIn(InternalTangleApi::class)
internal object ClassNames {

  val tangleViewModelComponent = ClassName("tangle.inject.api", "TangleViewModelComponent")
  val tangleViewModelSubcomponent = ClassName("tangle.inject.api", "TangleViewModelSubcomponent")
  val tangleViewModelSubcomponentFactory = tangleViewModelSubcomponent.nestedClass("Factory")
  val tangleViewModelKey = ClassName("tangle.inject.api", "ViewModelKey")
  val tangleViewModelProviderMap = TangleViewModelProviderMap::class.asClassName()
  val tangleViewModelProviderMapKeySet = TangleViewModelProviderMap.KeySet::class.asClassName()
  val tangleFragmentFactory = ClassName("tangle.inject.api", "TangleFragmentFactory")
  val tangleFragmentSubcomponent = ClassName("tangle.inject.api", "TangleFragmentSubcomponent")
  val tangleFragmentSubcomponentFactory = tangleFragmentSubcomponent.nestedClass("Factory")
  val tangleFragmentKey = ClassName("tangle.inject.api", "FragmentKey")
  val tangleFragmentProviderMap = TangleFragmentProviderMap::class.asClassName()

  val internalTangleApi = ClassName("tangle.inject.annotations.internal", "InternalTangleApi")
  val optIn = ClassName("kotlin", "OptIn")

  val tangleScope = TangleScope::class.asClassName()

  val androidxViewModel = ClassName("androidx.lifecycle", "ViewModel")
  val androidxFragment = ClassName("androidx.fragment.app", "Fragment")
  val androidxFragmentFactory = ClassName("androidx.fragment.app", "FragmentFactory")
  val androidxSavedStateHandle = ClassName("androidx.lifecycle", "SavedStateHandle")

  val javaClassOutFragment = Class::class.asClassName()
    .parameterizedBy(TypeVariableName("out·${androidxFragment.canonicalName}"))
  val javaClassOutVM = Class::class.asClassName()
    .parameterizedBy(TypeVariableName("out·${androidxViewModel.canonicalName}"))

  val fragmentMap = Map::class.asClassName().parameterizedBy(
    javaClassOutFragment,
    androidxFragment.jvmSuppressWildcards()
  )
  val provider = Provider::class.asClassName()
  val fragmentProviderMap = Map::class.asClassName().parameterizedBy(
    javaClassOutFragment,
    provider.parameterizedBy(androidxFragment.jvmSuppressWildcards()).jvmSuppressWildcards()
  )

  val daggerFactory = Factory::class.asClassName()
  val contributesTo = ContributesTo::class.asClassName()
  val mergeComponent = MergeComponent::class.asClassName()
  val mergeSubomponent = MergeSubcomponent::class.asClassName()
  val binds = Binds::class.asClassName()
  val bindsInstance = BindsInstance::class.asClassName()
  val subcomponentFactory = Subcomponent.Factory::class.asClassName()
  val intoMap = IntoMap::class.asClassName()
  val intoSet = IntoSet::class.asClassName()
  val provides = Provides::class.asClassName()
  val module = Module::class.asClassName()
  val multibinds = Multibinds::class.asClassName()

  val instanceFactory = InstanceFactory::class.asClassName()

  val inject = Inject::class.asClassName()
  val jvmStatic = JvmStatic::class.asClassName()
  val providerSavedStateHandle = Provider::class.asClassName()
    .parameterizedBy(androidxSavedStateHandle)

  val bundle = ClassName("android.os", "Bundle")
  val iBinder = ClassName("android.os", "IBinder")
  val parcelable = ClassName("android.os", "Parcelable")
  val size = ClassName("android.util", "Size")
  val sizeF = ClassName("android.util", "SizeF")
}
