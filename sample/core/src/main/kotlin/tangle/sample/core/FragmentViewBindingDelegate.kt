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

package tangle.sample.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * https://github.com/Zhuinden/fragmentviewbindingdelegate-kt
 */
class FragmentViewBindingDelegate<T : ViewBinding>(
  val fragment: Fragment,
  val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
  private var binding: T? = null

  init {
    fragment.lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        val viewLifecycleOwnerLiveDataObserver =
          Observer<LifecycleOwner?> {
            val viewLifecycleOwner = it ?: return@Observer

            viewLifecycleOwner.lifecycle.addObserver(
              object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                  binding = null
                }
              }
            )
          }

        override fun onCreate(owner: LifecycleOwner) {
          fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
        }

        override fun onDestroy(owner: LifecycleOwner) {
          fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
        }
      }
    )
  }

  override fun getValue(
    thisRef: Fragment,
    property: KProperty<*>
  ): T {
    val binding = binding
    if (binding != null) {
      return binding
    }

    val lifecycle = fragment.viewLifecycleOwner.lifecycle
    if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
      throw IllegalStateException(
        "Should not attempt to get bindings when Fragment views are destroyed."
      )
    }

    return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
  }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
  FragmentViewBindingDelegate(this, viewBindingFactory)
