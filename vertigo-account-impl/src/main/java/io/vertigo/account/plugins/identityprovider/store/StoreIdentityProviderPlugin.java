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
package io.vertigo.account.plugins.identityprovider.store;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertigo.account.impl.identityprovider.IdentityProviderPlugin;
import io.vertigo.app.Home;
import io.vertigo.core.component.Activeable;
import io.vertigo.dynamo.criteria.Criteria;
import io.vertigo.dynamo.criteria.Criterions;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.file.model.VFile;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.lang.Assertion;

/**
 * Source of identity.
 * @author npiedeloup
 */
public final class StoreIdentityProviderPlugin implements IdentityProviderPlugin, Activeable {

	private final StoreManager storeManager;

	private final String userIdentityEntity;
	private final String userAuthField;
	private DtDefinition userIdentityDefinition;

	/**
	 * Constructor.
	 * @param userIdentityEntity Entity name of userIdentityEntity
	 * @param userAuthField FieldName use to find user by it's authToken
	 * @param storeManager Store Manager
	 */
	@Inject
	public StoreIdentityProviderPlugin(
			@Named("userIdentityEntity") final String userIdentityEntity,
			@Named("userAuthField") final String userAuthField,
			final StoreManager storeManager) {
		Assertion.checkArgNotEmpty(userIdentityEntity);
		Assertion.checkArgNotEmpty(userAuthField);
		Assertion.checkNotNull(storeManager);
		this.userIdentityEntity = userIdentityEntity;
		this.userAuthField = userAuthField;
		this.storeManager = storeManager;
	}

	/** {@inheritDoc} */
	@Override
	public <E extends Entity> E getUserByAuthToken(final String userAuthToken) {
		final Criteria<Entity> criteriaByAuthToken = Criterions.isEqualTo(() -> userAuthField, userAuthToken);
		final DtList<Entity> results = storeManager.getDataStore().find(userIdentityDefinition, criteriaByAuthToken);
		Assertion.checkState(results.size() <= 1, "Too many matching for authToken {0}", userAuthToken);
		Assertion.checkState(!results.isEmpty(), "No user found for this authToken {0}", userAuthToken);

		return (E) results.get(0);
	}

	/** {@inheritDoc} */
	@Override
	public long getUsersCount() {
		return storeManager.getDataStore().count(userIdentityDefinition);
	}

	/** {@inheritDoc} */
	@Override
	public <E extends Entity> List<E> getAllUsers() {
		return storeManager.getDataStore().find(userIdentityDefinition, Criterions.alwaysTrue());
	}

	/** {@inheritDoc} */
	@Override
	public <E extends Entity> Optional<VFile> getPhoto(final URI<E> userURI) {
		return Optional.empty();
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		userIdentityDefinition = Home.getApp().getDefinitionSpace().resolve(userIdentityEntity, DtDefinition.class);
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		//nothing
	}

}
