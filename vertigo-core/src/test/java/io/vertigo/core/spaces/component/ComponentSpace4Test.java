/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.core.spaces.component;

import org.junit.Assert;
import org.junit.Test;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.app.config.AppConfig;
import io.vertigo.app.config.AppConfigBuilder;
import io.vertigo.app.config.LogConfig;
import io.vertigo.core.spaces.component.data.StartedManager;
import io.vertigo.core.spaces.component.data.StartedManagerImpl;
import io.vertigo.core.spaces.component.data.StartedManagerInitializer;

public final class ComponentSpace4Test {

	@Test
	public void testStartedComponent() {
		// @formatter:off
		final AppConfig appConfig = new AppConfigBuilder()
			.beginBoot()
				.withLogConfig(new LogConfig("/log4j.xml"))
			.endBoot()
			.beginModule("Started")
				.addComponent(StartedManager.class, StartedManagerImpl.class)
			.endModule()
			.addInitializer(StartedManagerInitializer.class)
		.build();
		// @formatter:on
		final StartedManager startedManager;
		try (AutoCloseableApp app = new AutoCloseableApp(appConfig)) {
			startedManager = app.getComponentSpace().resolve(StartedManager.class);
			Assert.assertTrue("Component StartedManager not Started", startedManager.isStarted());
			Assert.assertTrue("Component StartedManager not PostStarted", startedManager.isPostStarted());
			Assert.assertTrue("Component StartedManager not Initialized", startedManager.isInitialized());
		}
		Assert.assertFalse("Component StartedManager not Stopped", startedManager.isStarted());
	}
}