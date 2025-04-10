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

package tangle.sample.app.di

import android.content.Context
import androidx.work.WorkManager
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import tangle.sample.app.BuildConfig
import tangle.sample.core.AppScope
import tangle.sample.data.DogApiKey

@Module
@ContributesTo(AppScope::class)
object AppModule {
  @Provides
  fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)

  @DogApiKey
  @Provides
  fun provideDogApiKey() = BuildConfig.DOG_API_KEY
}
