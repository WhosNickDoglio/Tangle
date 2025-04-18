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

package tangle.sample.ui.fragmentsWithManualNavigation.breedList

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import tangle.sample.data.breed.BreedDao
import tangle.viewmodel.VMInject

class BreedListViewModel
  @VMInject
  constructor(
    val breedDoa: BreedDao
  ) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    val pagingDataFlow =
      Pager(
        config =
          PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            enablePlaceholders = false
          ),
        pagingSourceFactory = { breedDoa.pagingSource() }
      )
        .flow
  }
