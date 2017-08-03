/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.persona.impl.security;

import io.vertigo.app.config.Features;
import io.vertigo.core.param.Param;
import io.vertigo.persona.security.UserSession;
import io.vertigo.persona.security.VSecurityManager;

/**
 * Defines persona features.
 *
 * @author pchretien
 * @deprecated Use new account security management instead
 */
@Deprecated
public final class PersonaFeatures extends Features {

	/**
	 * Constructor.
	 */
	public PersonaFeatures() {
		super("persona");
	}

	/**
	 * Activates user session.
	 * @param userSessionClass the user session class
	 * @return these features
	 */
	public PersonaFeatures withUserSession(final Class<? extends UserSession> userSessionClass) {
		getModuleConfigBuilder()
				.addComponent(VSecurityManager.class, VSecurityManagerImpl.class,
						Param.of("userSessionClassName", userSessionClass.getName()));
		return this;
	}

	/** {@inheritDoc} */
	@Override
	protected void buildFeatures() {
		//		getModuleConfigBuilder()
		//				.addComponent(VSecurityManager.class, VSecurityManagerImpl.class);
	}
}