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

package tangle.viewmodel.activity

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import tangle.inject.InternalTangleApi
import tangle.viewmodel.internal.TangleViewModelFactory
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Equivalent to the Androidx ktx `by viewModels()` delegate.
 *
 * @sample tangle.viewmodel.samples.TangleActivityDelegateSample.byTangleViewModelSample
 * @return lazy [ViewModel] instance of the specified type, injected by Tangle/Anvil/Dagger
 * @since 0.11.0
 */
@OptIn(InternalTangleApi::class)
public inline fun <reified VM : ViewModel> ComponentActivity.tangleViewModel(): Lazy<VM> =
  lazy(NONE) {
    val viewModelFactory =
      TangleViewModelFactory(
        owner = this,
        defaultArgs = intent.extras,
        defaultFactory = defaultViewModelProviderFactory
      )

    ViewModelLazy(VM::class, { viewModelStore }, { viewModelFactory }).value
  }
