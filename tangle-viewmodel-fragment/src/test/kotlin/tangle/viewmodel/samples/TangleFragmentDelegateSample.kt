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

package tangle.viewmodel.samples

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import tangle.inject.test.utils.Sample
import tangle.viewmodel.VMInject
import tangle.viewmodel.fragment.tangleViewModel

class TangleFragmentDelegateSample {
  @Sample
  fun byTangleViewModelSample() {
    class MyFragment : Fragment() {
      val viewModel: MyViewModel by tangleViewModel()
    }
  }
}

class MyViewModel
  @VMInject
  constructor() : ViewModel()
