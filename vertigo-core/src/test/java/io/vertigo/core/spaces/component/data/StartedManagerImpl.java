/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.core.spaces.component.data;

import javax.inject.Inject;

import io.vertigo.app.Home;
import io.vertigo.core.component.Activeable;

public final class StartedManagerImpl implements StartedManager, Activeable {
	private boolean componentInitialized = false;
	private boolean componentStarted = false;
	private boolean appPreActivated = false;

	/**
	 * Construct an instance of DaemonManagerImpl.
	 */
	@Inject
	public StartedManagerImpl() {
		Home.getApp().registerPreActivateFunction(() -> appPreActivated = true);
	}

	@Override
	public void start() {
		componentStarted = true;
	}

	@Override
	public void stop() {
		componentStarted = false;
	}

	@Override
	public void init() {
		componentInitialized = true;
	}

	@Override
	public boolean isStarted() {
		return componentStarted;
	}

	@Override
	public boolean isAppPreActivated() {
		return appPreActivated;
	}

	@Override
	public boolean isInitialized() {
		return componentInitialized;
	}

}
