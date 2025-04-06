/*
 * Copyright (C) 2025 Rick Busarow
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

package tangle.sample.data

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import tangle.sample.core.AppScope
import javax.inject.Inject

interface CoroutineDispatcherProvider {
  val io: CoroutineDispatcher
}

@ContributesBinding(AppScope::class)
class CoroutineDispatcherProviderImpl
  @Inject
  constructor() : CoroutineDispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
  }
