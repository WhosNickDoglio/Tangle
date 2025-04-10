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

package tangle.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * Creates a [ListenableWorker] using Dagger for the [TangleWorkerFactory].
 *
 * @since 0.12.0
 */
public fun interface AssistedWorkerFactory<T : ListenableWorker> {
  /**
   * Creates a [ListenableWorker] of type `T`.
   *
   * @since 0.12.0
   */
  public fun create(
    context: Context,
    params: WorkerParameters
  ): T
}
