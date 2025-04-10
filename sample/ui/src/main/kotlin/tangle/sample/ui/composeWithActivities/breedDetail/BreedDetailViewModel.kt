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

package tangle.sample.ui.composeWithActivities.breedDetail

import androidx.lifecycle.ViewModel
import dispatch.core.MainCoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import tangle.inject.TangleParam
import tangle.sample.core.TextToSpeechDelegate
import tangle.sample.data.breed.BreedDao
import tangle.viewmodel.VMInject

class BreedDetailViewModel
  @VMInject
  constructor(
    private val breedDao: BreedDao,
    @TangleParam("breedId")
    private val breedId: Int,
    private val textToSpeechDelegate: TextToSpeechDelegate,
    coroutineScope: MainCoroutineScope
  ) : ViewModel() {
    val detailFlow =
      flow {
        val summary = breedDao.getById(breedId)
        emit(summary)
      }.stateIn(coroutineScope, SharingStarted.Eagerly, initialValue = null)

    fun onTextSelected(text: String) {
      textToSpeechDelegate.speak(text)
    }
  }
