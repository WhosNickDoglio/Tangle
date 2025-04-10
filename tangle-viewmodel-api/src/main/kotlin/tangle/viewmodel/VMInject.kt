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

package tangle.viewmodel

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR

/**
 * Annotates the constructor of a [ViewModel][androidx.lifecycle.ViewModel]
 * which will be injected via Tangle.
 *
 * These ViewModels are scoped to the [TangleViewModelMapSubcomponent],
 * which can be accessed via the [tangleViewModel] delegate function.
 *
 * @sample tangle.viewmodel.samples.VMInjectSample.vmInjectSample
 * @since 0.10.0
 */
@Target(CONSTRUCTOR)
@Retention(BINARY)
@MustBeDocumented
public annotation class VMInject
