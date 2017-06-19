/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.vertigo.account.plugins.authentication.store;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertigo.account.authentication.AuthenticationToken;
import io.vertigo.account.impl.authentication.AuthenticationRealmPlugin;
import io.vertigo.account.impl.authentication.UsernameAuthenticationToken;
import io.vertigo.account.impl.authentication.UsernamePasswordAuthenticationToken;
import io.vertigo.app.Home;
import io.vertigo.core.component.Activeable;
import io.vertigo.dynamo.criteria.Criteria;
import io.vertigo.dynamo.criteria.Criterions;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.lang.Assertion;

/**
 * A Store implementation of the Realm interface that
 * @author npiedeloup
 */
public class StoreAuthenticationRealmPlugin implements AuthenticationRealmPlugin, Activeable {

	private final StoreManager storeManager;
	private final String userCredentialEntity;
	private final String userLoginField;
	private final String userPasswordField;
	private final String userTokenIdField;
	private DtDefinition userCredentialDefinition;
	private UsernamePasswordAuthenticationToken defaultUserTrustedCredential;

	/**
	 * Constructor.
	 * @param storeManager Store Manager
	 * @param userCredentialEntity Entity name of userCredentialObject
	 * @param userLoginField Login fieldName
	 * @param userPasswordField Encoded Password fieldName
	 * @param userTokenIdField TokenId fieldName
	 */
	@Inject
	public StoreAuthenticationRealmPlugin(@Named("userCredentialEntity") final String userCredentialEntity,
			@Named("userLoginField") final String userLoginField,
			@Named("userPasswordField") final String userPasswordField,
			@Named("userTokenIdField") final String userTokenIdField,
			final StoreManager storeManager) {
		Assertion.checkNotNull(storeManager);
		Assertion.checkArgNotEmpty(userLoginField);
		Assertion.checkArgNotEmpty(userPasswordField);
		// -----
		this.storeManager = storeManager;
		this.userCredentialEntity = userCredentialEntity;
		this.userLoginField = userLoginField;
		this.userPasswordField = userPasswordField;
		this.userTokenIdField = userTokenIdField;

	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final AuthenticationToken token) {
		return token instanceof UsernameAuthenticationToken
				|| token instanceof UsernamePasswordAuthenticationToken;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<String> authenticateAccount(final AuthenticationToken token) {
		final Criteria criteriaByLogin = Criterions.isEqualTo(() -> userLoginField, token.getPrincipal());
		final DtList<DtObject> results = storeManager.getDataStore().find(userCredentialDefinition, criteriaByLogin);
		//may ensure, that valid or invalid login took the same time, so we don't assert no result here
		Assertion.checkState(results.size() <= 1, "Too many matching credentials for {0}", token.getPrincipal());

		final AuthenticationToken trustedAuthenticationToken;
		if (token instanceof UsernamePasswordAuthenticationToken) {
			if (results.isEmpty()) {
				trustedAuthenticationToken = defaultUserTrustedCredential;
			} else {
				final String trustedEncodedPassword = (String) userCredentialDefinition.getField(userPasswordField).getDataAccessor().getValue(results.get(0));
				trustedAuthenticationToken = new UsernamePasswordAuthenticationToken(token.getPrincipal(), trustedEncodedPassword);
			}
		} else {
			if (results.isEmpty()) {
				trustedAuthenticationToken = defaultUserTrustedCredential;
			} else {
				trustedAuthenticationToken = new UsernameAuthenticationToken(token.getPrincipal());
			}

		}
		//may ensure, that valid or invalid login took the same time, so we don't assert no result here
		if (token.match(trustedAuthenticationToken) //tokens match
				&& !results.isEmpty()) {//and Username exists (after)
			final String userTokenId = (String) userCredentialDefinition.getField(userTokenIdField).getDataAccessor().getValue(results.get(0));
			return Optional.of(userTokenId);
		}
		return Optional.empty();
	}

	/** {@inheritDoc} */
	@Override
	public void start() {
		userCredentialDefinition = Home.getApp().getDefinitionSpace().resolve(userCredentialEntity, DtDefinition.class);
		defaultUserTrustedCredential = new UsernamePasswordAuthenticationToken("defaultLogin", "defaultPassword");
	}

	/** {@inheritDoc} */
	@Override
	public void stop() {
		//nothing
	}
}
